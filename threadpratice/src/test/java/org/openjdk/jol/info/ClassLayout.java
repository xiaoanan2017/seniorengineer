//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.openjdk.jol.info;

import org.openjdk.jol.datamodel.DataModel;
import org.openjdk.jol.layouters.CurrentLayouter;
import org.openjdk.jol.layouters.Layouter;
import org.openjdk.jol.util.ClassUtils;
import org.openjdk.jol.util.ObjectUtils;
import org.openjdk.jol.vm.VM;
import org.openjdk.jol.vm.VirtualMachine;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Objects;
import java.util.SortedSet;

public class ClassLayout {
    private final ClassData classData;
    private final SortedSet<FieldLayout> fields;
    private final DataModel model;
    private final long size;
    private final int lossesInternal;
    private final int lossesExternal;
    private final int lossesTotal;
    static final String[] ZERO_RUNS = new String[16];

    public static ClassLayout parseClass(Class<?> klass) {
        return parseClass(klass, new CurrentLayouter());
    }

    public static ClassLayout parseClass(Class<?> klass, Layouter layouter) {
        return layouter.layout(ClassData.parseClass(klass));
    }

    public static ClassLayout parseInstance(Object instance) {
        return parseInstance(instance, new CurrentLayouter());
    }

    public static ClassLayout parseInstance(Object instance, Layouter layouter) {
        return layouter.layout(ClassData.parseInstance(instance));
    }

    private ClassLayout(ClassData classData, SortedSet<FieldLayout> fields, DataModel model, long instanceSize, int lossesInternal, int lossesExternal, int lossesTotal) {
        this.classData = classData;
        this.fields = fields;
        this.model = model;
        this.size = instanceSize;
        this.lossesInternal = lossesInternal;
        this.lossesExternal = lossesExternal;
        this.lossesTotal = lossesTotal;
    }

    public static ClassLayout create(ClassData classData, SortedSet<FieldLayout> fields, DataModel model, long instanceSize, boolean check) {
        if (check) {
            checkInvariants(fields, instanceSize);
        }

        long next = (long)model.headerSize();
        long internal = 0L;

        FieldLayout fl;
        for(Iterator var10 = fields.iterator(); var10.hasNext(); next = fl.offset() + fl.size()) {
            fl = (FieldLayout)var10.next();
            if (fl.offset() > next) {
                internal += fl.offset() - next;
            }
        }

        long external = instanceSize != next ? instanceSize - next : 0L;
        long total = internal + external;
        return new ClassLayout(classData, fields, model, instanceSize, (int)internal, (int)external, (int)total);
    }

    private static void checkInvariants(SortedSet<FieldLayout> fields, long instanceSize) {
        FieldLayout lastField = null;

        FieldLayout f;
        for(Iterator var4 = fields.iterator(); var4.hasNext(); lastField = f) {
            f = (FieldLayout)var4.next();
            if (f.offset() % f.size() != 0L) {
                throw new IllegalStateException("Field " + f + " is not aligned");
            }

            if (f.offset() + f.size() > instanceSize) {
                throw new IllegalStateException("Field " + f + " is overflowing the object of size " + instanceSize);
            }

            if (lastField != null && f.offset() < lastField.offset() + lastField.size()) {
                throw new IllegalStateException("Field " + f + " overlaps with the previous field " + lastField);
            }
        }

    }

    public SortedSet<FieldLayout> fields() {
        return this.fields;
    }

    public long instanceSize() {
        return this.size;
    }

    public int headerSize() {
        return this.model.headerSize();
    }

    public long getLossesInternal() {
        return (long)this.lossesInternal;
    }

    public long getLossesExternal() {
        return (long)this.lossesExternal;
    }

    public long getLossesTotal() {
        return (long)this.lossesTotal;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator var2 = this.fields().iterator();

        while(var2.hasNext()) {
            FieldLayout f = (FieldLayout)var2.next();
            sb.append(f).append("\n");
        }

        sb.append("size = ").append(this.size).append("\n");
        return sb.toString();
    }

    public String toPrintable() {
        return this.toPrintable(this.classData.instance());
    }

    public String toPrintable(Object instance) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        int maxTypeLen = "TYPE".length();

        FieldLayout f;
        for(Iterator var5 = this.fields().iterator(); var5.hasNext(); maxTypeLen = Math.max(f.typeClass().length(), maxTypeLen)) {
            f = (FieldLayout)var5.next();
        }

        maxTypeLen += 2;
        String MSG_OBJ_HEADER = "(object header)";
        String MSG_MARK_WORD = "(object header: mark)";
        String MSG_CLASS_WORD = "(object header: class)";
        String MSG_ARR_LEN = "(array length)";
        String MSG_FIELD_GAP = "(alignment/padding gap)";
        String MSG_OBJ_GAP = "(object alignment gap)";
        int maxDescrLen = "DESCRIPTION".length();
        maxDescrLen = Math.max(maxDescrLen, MSG_OBJ_HEADER.length());
        maxDescrLen = Math.max(maxDescrLen, MSG_MARK_WORD.length());
        maxDescrLen = Math.max(maxDescrLen, MSG_CLASS_WORD.length());
        maxDescrLen = Math.max(maxDescrLen, MSG_FIELD_GAP.length());
        maxDescrLen = Math.max(maxDescrLen, MSG_OBJ_GAP.length());

        for(Iterator var12 = this.fields().iterator(); var12.hasNext(); maxDescrLen = Math.max(f.shortFieldName().length(), maxDescrLen)) {
            f = (FieldLayout)var12.next();
        }

        maxDescrLen += 2;
        String format = "%3d %3d %" + maxTypeLen + "s %-" + maxDescrLen + "s %s%n";
        String formatS = "%3s %3s %" + maxTypeLen + "s %-" + maxDescrLen + "s %s%n";
        if (instance == null) {
            try {
                Class<?> klass = ClassUtils.loadClass(this.classData.name());
                if (!klass.isAssignableFrom(instance.getClass())) {
                    throw new IllegalArgumentException("Passed instance type " + instance.getClass() + " is not assignable from " + klass + ".");
                }
            } catch (ClassNotFoundException var28) {
                throw new IllegalArgumentException("Class is not found: " + this.classData.name() + ".");
            }
        }

        pw.println(this.classData.name() + " object internals:");
        pw.printf(formatS, "OFF", "SZ", "TYPE", "DESCRIPTION", "VALUE");
        String markStr = "N/A";
        String classStr = "N/A";
        String arrLenStr = "N/A";
        int markSize = this.model.markHeaderSize();
        int classSize = this.model.classHeaderSize();
        int arrSize = this.model.arrayLengthHeaderSize();
        int markOffset = 0;
        int classOffset = markOffset + markSize;
        int arrOffset = classOffset + classSize;
        if (instance == null) {
            VirtualMachine vm = VM.current();
            if (markSize == 8) {
                long mark = vm.getLong(instance, (long)markOffset);
                markStr = toHex(mark) + " " + parseMarkWord(mark);
            } else if (markSize == 4) {
                int mark = vm.getInt(instance, (long)markOffset);
                markStr = toHex(mark) + " " + parseMarkWord(mark);
            }

            if (classSize == 8) {
                classStr = toHex(vm.getLong(instance, (long)classOffset));
            } else if (classSize == 4) {
                classStr = toHex(vm.getInt(instance, (long)classOffset));
            }

            if (this.classData.isArray()) {
                arrLenStr = Integer.toString(vm.getInt(instance, (long)arrOffset));
            }
        }

        pw.printf(format, Integer.valueOf(markOffset), markSize, "", MSG_MARK_WORD, markStr);
        pw.printf(format, classOffset, classSize, "", MSG_CLASS_WORD, classStr);
        if (this.classData.isArray()) {
            pw.printf(format, arrOffset, arrSize, "", MSG_ARR_LEN, arrLenStr);
        }

        long nextFree = (long)this.headerSize();

        for(Iterator var25 = this.fields().iterator(); var25.hasNext(); nextFree = f.offset() + f.size()) {
            f = (FieldLayout)var25.next();
            if (f.offset() > nextFree) {
                pw.printf(format, nextFree, f.offset() - nextFree, "", MSG_FIELD_GAP, "");
            }

            Field fi = f.data().refField();
            pw.printf(format, f.offset(), f.size(), f.typeClass(), f.shortFieldName(), instance != null && fi != null ? ObjectUtils.safeToString(ObjectUtils.value(instance, fi)) : "N/A");
        }

        long sizeOf = instance != null ? VM.current().sizeOf(instance) : this.instanceSize();
        if (sizeOf != nextFree) {
            pw.printf(format, nextFree, this.lossesExternal, "", MSG_OBJ_GAP, "");
        }

        pw.printf("Instance size: %d bytes%n", sizeOf);
        pw.printf("Space losses: %d bytes internal + %d bytes external = %d bytes total%n", this.lossesInternal, this.lossesExternal, this.lossesTotal);
        pw.close();
        return sw.toString();
    }

    private static String toHex(int x) {
        String s = Integer.toHexString(x);
        int deficit = 8 - s.length();
        return "0x" + ZERO_RUNS[deficit] + s;
    }

    private static String toHex(long x) {
        String s = Long.toHexString(x);
        int deficit = 16 - s.length();
        return "0x" + ZERO_RUNS[deficit] + s;
    }

    private static String parseMarkWord(int mark) {
        int bits = mark & 3;
        switch(bits) {
            case 0:
                return "(thin lock: " + toHex(mark) + ")";
            case 1:
                String s = "; age: " + (mark >> 3 & 15);
                int tribits = mark & 7;
                switch(tribits) {
                    case 1:
                        int hash = mark >>> 7;
                        if (hash != 0) {
                            return "(hash: " + toHex(hash) + s + ")";
                        }

                        return "(non-biasable" + s + ")";
                    case 5:
                        int thread = mark >>> 9;
                        if (thread == 0) {
                            return "(biasable" + s + ")";
                        }

                        return "(biased: " + toHex(thread) + "; epoch: " + (mark >> 7 & 2) + s + ")";
                }
            default:
                return "(parse error)";
            case 2:
                return "(fat lock: " + toHex(mark) + ")";
            case 3:
                return "(marked: " + toHex(mark) + ")";
        }
    }

    private static String parseMarkWord(long mark) {
        long bits = mark & 3L;
        switch((int)bits) {
            case 0:
                return "(thin lock: " + toHex(mark) + ")";
            case 1:
                String s = "; age: " + (mark >> 3 & 15L);
                int tribits = (int)(mark & 7L);
                switch(tribits) {
                    case 1:
                        int hash = (int)(mark >>> 8);
                        if (hash != 0) {
                            return "(hash: " + toHex(hash) + s + ")";
                        }

                        return "(non-biasable" + s + ")";
                    case 5:
                        long thread = mark >>> 10;
                        if (thread == 0L) {
                            return "(biasable" + s + ")";
                        }

                        return "(biased: " + toHex(thread) + "; epoch: " + (mark >> 8 & 2L) + s + ")";
                }
            default:
                return "(parse error)";
            case 2:
                return "(fat lock: " + toHex(mark) + ")";
            case 3:
                return "(marked: " + toHex(mark) + ")";
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ClassLayout that = (ClassLayout)o;
            return this.fields.equals(that.fields) && this.model.equals(that.model);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.fields, this.model});
    }

    static {
        String s = "";

        for(int c = 0; c < ZERO_RUNS.length; ++c) {
            ZERO_RUNS[c] = s;
            s = s + "0";
        }

    }

    public String toPrintableSimple() {
        return toPrintableSimple(classData.instance());
    }

    public String toPrintableSimple(Object instance) {
        StringBuilder sb = new StringBuilder();
        String markStr = "";
        String remind = "";

        int markSize = model.markHeaderSize();

        int markOffset = 0;

        if (instance == null) {
            VirtualMachine vm = VM.current();
            if (markSize == 8) {
                long mark = vm.getLong(instance, markOffset);
                markStr = Long.toBinaryString(mark);
                remind = parseMarkWord(mark);
            } else if (markSize == 4) {
                int mark = vm.getInt(instance, markOffset);
                markStr = Integer.toBinaryString(mark);
                remind = parseMarkWord(mark);
            }
        }

        // 高位补0
        int i = 1;
        for (; i <= 8 * markSize - markStr.length(); i++) {
            sb.append('0');
            if (i % 8 == 0) {
                sb.append(" ");
            }
        }
        for (; i <= 8 * markSize; i++) {
            sb.append(markStr.charAt(i - (8 * markSize - markStr.length()) - 1));
            if (i % 8 == 0) {
                sb.append(" ");
            }
        }

        sb.append(remind);

        return sb.toString();
    }
}

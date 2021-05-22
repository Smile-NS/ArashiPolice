package io.github.smile_ns.arashipolice.sqlite;

import java.util.ArrayList;

public class SQLiteValues extends ArrayList<SQLiteValues> {

    private Object value = null;

    public SQLiteValues set(Object val) {
        this.value = val;
        super.add((SQLiteValues) this.clone());
        return this;
    }

    @Override
    public SQLiteValues remove(int index) {
        this.value = null;
        return super.remove(index);
    }

    @Override
    public boolean remove(Object obj) {
        this.value = null;
        return super.remove(obj);
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0;i < this.size();i++) {
            SQLiteValues val = this.get(i);

            Object value = val.getValue();
            if (value == null) result.append("null");
            else if (value instanceof Number)
                result.append(value.toString());
            else if (value instanceof String || value instanceof Character)
                result.append("'").append(val.getValue().toString()).append("'");
            else result.append(value.toString());

            if (i < this.size() - 1) result.append(", ");
        }
        return result.toString();
    }
}

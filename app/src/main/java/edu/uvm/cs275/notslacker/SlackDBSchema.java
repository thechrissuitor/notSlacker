package edu.uvm.cs275.notslacker;

// this class is the schema for the database
public class SlackDBSchema {
    // this class contains information pertaining to the table
    public static final class SlackTable {
        // table name
        public static final String NAME = "slacks";
        // table columns
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DESCRIPTION = "description";
            public static final String DUE_DATE = "due_date";
            public static final String COMPLETED = "completed";
        }
    }

}

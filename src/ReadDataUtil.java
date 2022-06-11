import java.io.*;

public class ReadDataUtil {

    // Reads the position and velocity vector on the specified 'day' from the file with the
    // specified 'path', and sets position and current velocity of 'b' accordingly. If
    // successful the method returns 'true'. If the specified 'day' was not found in the file,
    // 'b' is unchanged and the method returns 'false'.
    // The file format is validated before reading the state.
    // Lines before the line "$$SOE" and after the line "$$EOE" the are ignored. Each line of the
    // file between the line "$$SOE" and the line "$$EOE" is required to have the following format:
    // JDTDB, TIME, X, Y, Z, VX, VY, VZ
    // where JDTDB is interpretable as a 'double' value, TIME is a string and X, Y, Z, VX, VY and
    // VZ are interpretable as 'double' values. JDTDB can be ignored. The character ',' must only
    // be used as field separator. If the file is not found, an exception of the class
    // 'StateFileNotFoundException' is thrown. If it does not comply with the format described
    // above, the method throws an exception of the class 'StateFileFormatException'. Both
    // exceptions are subtypes of 'IOException'.
    // Precondition: b != null, path != null, day != null and has the format YYYY-MM-DD.
    public static boolean readConfiguration(NamedBody b, String path, String day)
            throws IOException {

        File file = new File(path);
        if (!file.exists()) {
            throw new StateFileNotFoundException(file);
        }

        try (BufferedReader in = new BufferedReader(new FileReader(path))) {
            int lineNumber = 1;
            // Ignore all lines before $$SOE
            while (!in.readLine().equals("$$SOE")) {
                lineNumber++;
            }

            DataLine data;
            String line = in.readLine();
            lineNumber++;
            while (!line.equals("$$EOE")) {
                
                try {
                    data = new DataLine(line);
                } catch (StateFileFormatException e) {
                    throw new StateFileFormatException(e.getMessage() + " in file " + file.getAbsolutePath() + ":" + lineNumber);
                }
                if (data.date.equals(day)) {
                    b.setState(
                            new Vector3(data.x, data.y, data.z),
                            new Vector3(data.vx, data.vy, data.vz)
                    );
                    return true;
                }
                line = in.readLine();
                lineNumber++;
            }
        }

        return false;
    }

    static class DataLine {
        // JDTDB, TIME, X, Y, Z, VX, VY, VZ
        private final double jdtbd;
        private final String date;
        private final double x;
        private final double y;
        private final double z;
        private final double vx;
        private final double vy;
        private final double vz;


        public DataLine(String str) throws StateFileFormatException {
            String[] split = str.split(",");
            if (split.length != 8) {
                throw new StateFileFormatException("Line does not contain 8 segments. '" + str + "'");
            }
            try {
                this.jdtbd = Double.parseDouble(split[0].trim());
                this.date = split[1].trim().substring(5, 16);
                this.x = Double.parseDouble(split[2].trim());
                this.y = Double.parseDouble(split[3].trim());
                this.z = Double.parseDouble(split[4].trim());
                this.vx = Double.parseDouble(split[5].trim());
                this.vy = Double.parseDouble(split[6].trim());
                this.vz = Double.parseDouble(split[7].trim());
            } catch (NumberFormatException e) {
                throw new StateFileFormatException("Could not parse double: " + e.getMessage());
            }
        }

        @Override
        public String toString() {
            return "DataLine{" +
                    "jdtbd=" + jdtbd +
                    ", time='" + date + '\'' +
                    ", x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    ", vx=" + vx +
                    ", vy=" + vy +
                    ", vz=" + vz +
                    '}';
        }
    }

}


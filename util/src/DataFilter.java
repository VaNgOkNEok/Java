import java.io.*;
import java.nio.file.*;
import java.util.LinkedList;
import java.util.List;

public class DataFilter {
    private boolean addElemInFile;
    private StatisticsType typeStatistics = StatisticsType.NONE;
    private String prefix = "";
    private Path outputFilesDirectory = null;
    private List<String> nameFiles = new LinkedList<>();
    private Statistics statistics;

    public StatisticsType getTypeStatistics()    {
        return typeStatistics;
    }

    private boolean doesContainProhibitedSign(String line) {
        return line.matches(".*[<>:/\\\\|?*\"].*");
    }

    private boolean isOption(String line) {
        return line.matches("-[a-zA-Z]+");
    }

    public DataFilter(String[] inputString) {
        for (int i = 0; i < inputString.length; i++) {
            if (inputString[i].equals("-o") && i + 1 < inputString.length) {
                if (isOption(inputString[i + 1])) continue;
                outputFilesDirectory = Paths.get(inputString[++i]);
                continue;
            }
            if (inputString[i].equals("-p") && i + 1 < inputString.length) {
                if (doesContainProhibitedSign(inputString[i + 1])) {
                    i++;
                } else {
                    prefix = inputString[++i];
                }
                continue;
            }
            if (inputString[i].equals("-a")) {
                addElemInFile = true;
                continue;
            }
            if (inputString[i].equals("-s")) {
                typeStatistics = StatisticsType.BRIEF;
                continue;
            }
            if (inputString[i].equals("-f")) {
                typeStatistics = StatisticsType.FULL;
                continue;
            }
            if (isOption(inputString[i])) {
                System.err.println("Warning: Unrecognized character: " + inputString[i]);
                continue;
            }
            nameFiles.add(inputString[i]);
        }
    }

    private Path getOutputFilePath(String fileName) {
        if (outputFilesDirectory != null) {
            try {
                if (Files.notExists(outputFilesDirectory)) {
                    Files.createDirectories(outputFilesDirectory);
                }

                Path dir = outputFilesDirectory.normalize().toAbsolutePath();

                if (!Files.isWritable(dir)) {
                    return Paths.get(prefix + fileName);
                }

                return dir.resolve(prefix + fileName);

            } catch (IOException e) {
                System.err.println("ERROR: Error with the path " + outputFilesDirectory);
            }
        }

        return Paths.get(prefix + fileName);
    }

    private BufferedWriter creatingOutputFileAndStream(String fileName) throws IOException {
        Path outputFilePath = getOutputFilePath(fileName);
        return (addElemInFile) ? Files.newBufferedWriter(outputFilePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND) :
                Files.newBufferedWriter(outputFilePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private boolean isStatisticsEnabled() {
        return typeStatistics != StatisticsType.NONE;
    }

    public void filter() throws IOException {

        if (isStatisticsEnabled()) {
            statistics = new Statistics(this);
            statistics.initializationOfStaticsParameters();
        }

        BufferedWriter bwf = null;
        BufferedWriter bws = null;
        BufferedWriter bwi = null;

        String line;
        boolean isWrite;

        try {
            for (String fileName : nameFiles) {
                Path inputFile = Paths.get(fileName);
                if (Files.notExists(inputFile)) {
                    System.err.println("Warning: File does not exist: " + fileName);
                    continue;
                }
                if (Files.exists(inputFile) && !Files.isDirectory(inputFile)) {
                    try (BufferedReader br = Files.newBufferedReader(inputFile)) {
                        while ((line = br.readLine()) != null) {
                            isWrite = false;
                            if (line.matches("[-+]?\\d+")) {
                                if (bwi == null) {
                                    bwi = creatingOutputFileAndStream("integers.txt");
                                }
                                bwi.write(line);
                                bwi.newLine();

                                isWrite = true;
                                if (isStatisticsEnabled()) statistics.addIntegerElement(line);
                            }
                            if (!isWrite) {
                                if (line.matches("[-+]?\\d+\\.\\d+")
                                        || line.matches("[-+]?\\d+\\.\\d+E[-+]\\d+")) {
                                    if (bwf == null) {
                                        bwf = creatingOutputFileAndStream("floats.txt");
                                    }
                                    bwf.write(line);
                                    bwf.newLine();

                                    isWrite = true;
                                    if (isStatisticsEnabled()) statistics.addFloatElement(line);
                                }
                            }
                            if (!isWrite) {
                                if (bws == null) {
                                    bws = creatingOutputFileAndStream("strings.txt");
                                }
                                bws.write(line);
                                bws.newLine();

                                if (isStatisticsEnabled()) statistics.addStringElement(line);
                            }
                        }
                    } catch (FileNotFoundException e) {
                        System.err.println("ERROR: File not found: " + fileName);
                    }
                }
            }
        } finally {
            if (bwi != null) {
                bwi.flush();
                bwi.close();
            }
            if (bwf != null) {
                bwf.flush();
                bwf.close();
            }
            if (bws != null) {
                bws.flush();
                bws.close();
            }
        }
        if (bwf != null || bws != null || bwi != null) {
            System.out.println(statistics);
        }
    }
}

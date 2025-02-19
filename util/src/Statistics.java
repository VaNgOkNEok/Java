import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

public class Statistics {
    private DataFilter dataFilter;

    private int cntInteger;
    private int cntFloat;
    private int cntString;

    private BigInteger maxInteger;
    private BigInteger minInteger;
    private BigInteger sumInteger;
    private BigInteger avgInteger;

    private BigDecimal maxFloat;
    private BigDecimal minFloat;
    private BigDecimal sumFloat;
    private BigDecimal avgFloat;

    private int maxStringLength;
    private int minStringLength;

    public Statistics(DataFilter dataFilter) {
        this.dataFilter = dataFilter;
    }

    public void initializationOfStaticsParameters() {
        switch (dataFilter.getTypeStatistics()) {
            case StatisticsType.FULL:
                sumInteger = BigInteger.ZERO;
                sumFloat = BigDecimal.ZERO;
                maxStringLength = 0;
                minStringLength = 0;
                maxInteger = null;
                minInteger = null;
                maxFloat = null;
                minFloat = null;
            case StatisticsType.BRIEF:
                cntFloat = 0;
                cntString = 0;
                cntInteger = 0;
                break;
        }
    }

    private boolean isFullStatistics() {
        return dataFilter.getTypeStatistics() == StatisticsType.FULL;
    }

    public void addIntegerElement(String line) {
        cntInteger++;
        if (isFullStatistics()) {
            BigInteger elem = new BigInteger(line);
            sumInteger = sumInteger.add(elem);
            maxInteger = (maxInteger == null) ? elem : elem.max(maxInteger);
            minInteger = (minInteger == null) ? elem : elem.min(minInteger);
        }
    }

    public void addFloatElement(String line) {
        cntFloat++;
        if (isFullStatistics()) {
            BigDecimal elem = new BigDecimal(line);
            sumFloat = sumFloat.add(elem);
            maxFloat = (maxFloat == null) ? elem : elem.max(maxFloat);
            minFloat = (minFloat == null) ? elem : elem.min(minFloat);
        }
    }

    public void addStringElement(String line) {
        cntString++;
        if (isFullStatistics()) {
            maxStringLength = (maxStringLength == 0) ? line.length() : Math.max(maxStringLength, line.length());
            minStringLength = (minStringLength == 0) ? line.length() : Math.min(minStringLength, line.length());
        }
    }

    @Override
    public String toString() {
        switch (dataFilter.getTypeStatistics()) {
            case StatisticsType.BRIEF:
                return String.format("Brief statistics:\n" +
                        "Number of strings - %d\n" +
                        "Number of integers - %d\n" +
                        "Number of floats numbers - %d\n", cntString, cntInteger, cntFloat);
            case StatisticsType.FULL:
                StringBuilder builder = new StringBuilder();
                avgFloat = (cntFloat > 0) ? sumFloat.divide(new BigDecimal(cntFloat)) : BigDecimal.ZERO;
                avgInteger = (cntInteger > 0) ? sumInteger.divide(new BigInteger(String.valueOf(cntInteger))) : BigInteger.ZERO;

                DecimalFormat df = new DecimalFormat("0.###E0");

                builder.append(String.format("Full statistics:\n" +
                        "Number of strings - %d\n" +
                        "Number of integers - %d\n" +
                        "Number of floats numbers - %d\n", cntString, cntInteger, cntFloat));

                if (cntInteger > 0) {
                    builder.append("-----------------------------------\n");
                    builder.append(String.format("The minimum value of integers - %d\n" +
                            "The maximum value of integers - %d\nThe sum of integers - %d\n" +
                            "The average of integers - %d\n", minInteger, maxInteger, sumInteger, avgInteger));
                }
                if (cntFloat > 0) {
                    builder.append("-----------------------------------\n");
                    builder.append(String.format("The minimum value of floats - %s\n" +
                            "The maximum value of floats - %s\n" +
                            "The sum of floats - %s\n" +
                            "The average of floats - %s\n", df.format(minFloat), df.format(maxFloat), df.format(sumFloat), df.format(avgFloat)));
                }

                if (cntString > 0) {
                    builder.append("-----------------------------------\n");
                    builder.append(String.format("Minimum line length - %d\n" +
                            "Maximum line length - %d\n", minStringLength, maxStringLength));
                }
                return builder.toString();
        }
        return null;
    }
}

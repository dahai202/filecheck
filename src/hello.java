import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @desc:
 * @author: wangjunhai
 * @create: 2020-03-17 16:43
 **/
public class hello {
    public static void main(String[] args) throws Throwable {
        System.out.println("receive data");
        Stream.of(args).forEach(x -> System.out.println("receive " + x));
        String oriPath = args[0];
        String wsdPath = args[1];
        String[] oriContent = readToString(oriPath, Charset.forName("utf-8"));
        String[] wsdContent = readToString(wsdPath, Charset.forName("GBK"));
        boolean b = compareFile(oriContent, wsdContent);
        System.out.println("result " + "\t" + (b ? "success" : "fail"));
    }

    private static boolean compareFile(String[] oriContent, String[] wsdContent) {
        if (oriContent == null && wsdContent == null) {
            return true;
        }

        if (oriContent.length != wsdContent.length) {
            System.out.println("number not right" + String.format("shucang%s,wangdai%s", wsdContent.length,
                    oriContent.length));
            return false;
        }

        List<String> diff = new ArrayList<>();
        for (String wsdItem : wsdContent) {
            boolean isExist = Arrays.stream(oriContent).anyMatch(oriItem -> wsdItem.equals(oriItem));
            if (!isExist) {
                diff.add(wsdItem);
            }
        }

        List<String> check = new ArrayList<>();
        if (diff.size() > 0) {
            for (String diffItem : diff) {
                String[] diffItems = diffItem.split("\\|");
                boolean isExist =
                        Arrays.stream(oriContent).filter(oriItem -> oriItem.contains(diffItems[1]) && oriItem.contains(diffItems[1])).anyMatch(oriItem -> compareLine(oriItem.split("\\|"), diffItems));
                if (!isExist) {
                    check.add(diffItem);
                }
            }
        }

        return check.size() == 0;
    }

    private static boolean compareLine(String[] lineOri, String[] lineWsd) {
        if (lineOri.length == lineWsd.length) {
            if (!compareItem(lineOri[0], lineWsd[0])) {
                return false;
            }
            return IntStream.range(0, lineOri.length).allMatch(i -> compareItem(lineOri[i], lineWsd[i]));
        }
        return false;
    }

    private static boolean compareItem(String itemOri, String itemWsd) {
        BiFunction<String, String, Boolean> compare = null;
        if (itemWsd.startsWith(".")) {
            compare = (x, y) -> itemOri.equals("0" + itemWsd);
        } else if (itemOri.startsWith(".")) {
            compare = (x, y) -> itemWsd.equals("0" + itemOri);
        } else if (itemOri.contains(".") && itemOri.contains("0")) {
            compare = (x, y) -> Double.valueOf(itemOri).equals(Double.valueOf(itemWsd));
        } else {
            compare = (x, y) -> itemOri.equals(itemWsd);
        }
        Boolean apply = compare.apply(itemOri, itemWsd);
        return apply;
    }

    private static String[] readToString(String filePath, Charset charset) throws IOException {
        File file = new File(filePath);
        boolean isAccountingFile = file.getName().startsWith("wsd_");
        List<String> lines = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), charset));
        String line;
        int i = 0;
        while ((line = br.readLine()) != null) {
            if (i == 0 && isAccountingFile) {
                continue;
            }
            lines.add(line);
        }
        br.close();
        return lines.toArray(new String[lines.size()]);
    }
}

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;

public class SearchTextInFile
{
    public static void main(String[] args) throws IOException
    {
        // First write a file, with large number of entries
        writeFile("/home/aqeel/temp/subscribers_files.csv");

        long scannerSearchMillis = 0;
        long brSearchMillis = 0;

        int iterations = 5;

        // Now search random strings five times, and see the time taken
        for (int i = 0; i < iterations; i++)
        {
            String msisdn = String.valueOf(923000000000l + ((long) (Math.random() * 40000000)));

            System.out.println("ITERATION " + i);
            System.out.print("Search " + msisdn + " using scanner");
            Date d1 = new Date();
            searchUsingScanner("/home/aqeel/temp/subscribers_files.csv", msisdn);
            Date d2 = new Date();

            long millis = (d2.getTime() - d1.getTime());
            scannerSearchMillis += millis;
            System.out.println(" | " + (millis / 1000) + " Seconds");
            System.out.println("==================================================================");
            System.out.print("Search " + msisdn + " using buffered reader");
            d1 = new Date();
            searchUsingBufferedReader("/home/aqeel/temp/subscribers_files.csv", msisdn);
            d2 = new Date();
            millis = d2.getTime() - d1.getTime();
            brSearchMillis += millis;
            System.out.println(" | " + (millis / 1000) + " Seconds");
            System.out.println("==================================================================");
            System.out.println("==================================================================");
            System.out.println("==================================================================");
            System.out.println("==================================================================");
        }

        System.out.println("Average Search time using Scanner " + (scannerSearchMillis / (iterations * 1000.0)) + " Seconds");
        System.out.println("Average Search time using BufferedReader " + (brSearchMillis / (iterations * 1000.0)) + " Seconds");
    }

    public static void writeFile(String path)
    {
        BufferedWriter csvWriter = null;
        HashSet<Integer> additions = new HashSet<Integer>();
        try
        {
            csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)));

            for (int i = 0; i < 40000000; i++)
            {
                int addition = (int) (Math.random() * 40000000);
                additions.add(addition);

                if (i % 20000 == 0)
                {
                    System.out.println("Entries written : " + i + " ------ Unique Entries: " + additions.size());
                    csvWriter.flush();
                }

                long msisdn = 923000000000l + addition;
                csvWriter.write(String.valueOf(msisdn) + "|" + String.valueOf((int) (Math.random() * 131)) + "\r\n");
            }

            csvWriter.flush();

            System.out.println("Unique Entries written : " + additions.size());
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            if (csvWriter != null)
            {
                try
                {
                    csvWriter.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static String searchUsingScanner(String filePath, String searchQuery) throws FileNotFoundException
    {
        searchQuery = searchQuery.trim();
        Scanner scanner = null;
        try
        {
            scanner = new Scanner(new File(filePath));
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                if (line.contains(searchQuery))
                {
                    return line;
                }
                else
                {
                }
            }
        }
        finally
        {
            try
            {
                if (scanner != null)
                    scanner.close();
            }
            catch (Exception e)
            {
                System.err.println("Exception while closing scanner " + e.toString());
            }
        }

        return null;
    }

    public static String searchUsingBufferedReader(String filePath, String searchQuery) throws IOException
    {
        searchQuery = searchQuery.trim();
        BufferedReader br = null;

        try
        {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line.contains(searchQuery))
                {
                    return line;
                }
                else
                {
                }
            }
        }
        finally
        {
            try
            {
                if (br != null)
                    br.close();
            }
            catch (Exception e)
            {
                System.err.println("Exception while closing bufferedreader " + e.toString());
            }
        }

        return null;
    }
}

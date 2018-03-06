
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
 
public interface ISIC {
 
    //void ReadSourceCode(FileInputStream file) throws FileNotFoundException;
 
  //  void objectCode(String fileName);
 
    void listingFile(FileInputStream file);
 
    boolean ReadSourceCode(String fileName) throws FileNotFoundException, UnsupportedEncodingException;
 
}
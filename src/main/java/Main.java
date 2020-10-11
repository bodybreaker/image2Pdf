import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {


    private static final String DESTINATION_FOLDER="pdf병합/";
    public static void main(String[] args) {


        File imgDir = new File(""); // 현재폴더
        File[] imgFiles = imgDir.listFiles();
        List<List<File>> imgList = new ArrayList<List<File>>();

        makeFolder(DESTINATION_FOLDER);

        int idx = 0;

        for(int i =0; i< imgFiles.length; i=idx){

            String subFileName = getSubFileName(imgFiles[i].getName());
            List<File> targetImages = new ArrayList<File>();

            targetImages.add(imgFiles[i]); // 최초1

            for(int j=i+1; j<imgFiles.length ; j++){
                if(subFileName.equals(getSubFileName(imgFiles[j].getName()))){
                    targetImages.add(imgFiles[j]);
                    idx = j+1;
                }
            }

            if(i==idx){
                idx++;
            }
            imgList.add(targetImages);

            try {
                img2pdf(targetImages);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //System.out.println(targetImages.size());
            // 여기서 pdf 한통 만들기

            //System.out.println(subFileName);
        }
        System.out.println("사이즈 : " + imgList.size());

    }

    public static String getSubFileName(String fileName){
        String subFileName = "";

        int idxSep = fileName.indexOf("_");
        if(idxSep>0){
            subFileName = fileName.substring(0,idxSep);
        }else{
            subFileName = fileName.substring(0,fileName.indexOf("."));
        }

        return subFileName;
    }



    public static void img2pdf(List<File> imgList) throws IOException {
        String name = "";
        PDDocument doc = new PDDocument();
        try {
            for(File imgFile : imgList){

                PDPage page = new PDPage();
                doc.addPage(page);
                PDImageXObject pdImageXObject = PDImageXObject.createFromFile(imgFile.getAbsolutePath(),doc);
                PDPageContentStream pdPageContentStream = new PDPageContentStream(doc,page);

                pdPageContentStream.drawImage(pdImageXObject,0,0,612,796);

                pdPageContentStream.close();
                doc.save(DESTINATION_FOLDER+getSubFileName(imgFile.getName())+".pdf");
                name = getSubFileName(imgFile.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            doc.close();
            System.out.println("변환완료 : >> "+DESTINATION_FOLDER+name+".pdf");
        }

    }

    private static void makeFolder(String path){
        File Folder = new File(path);

        if (!Folder.exists()) {
            try{
                Folder.mkdir();
            }
            catch(Exception e){
                e.getStackTrace();
            }
        }
    }
}

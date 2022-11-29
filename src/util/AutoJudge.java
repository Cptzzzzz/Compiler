package util;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class AutoJudge {
    private AutoJudge() {
    }

    private static AutoJudge autoJudge;

    public static AutoJudge getInstance() {
        if (autoJudge == null)
            autoJudge = new AutoJudge();
        return autoJudge;
    }
    public boolean mipsCompare(String filename1, String filename2){
        try {
            List<String> original = Files.readAllLines(new File(filename1).toPath());
            List<String> revised = Files.readAllLines(new File(filename2).toPath());
            Patch<String> patch = DiffUtils.diff(original, revised);
            for (AbstractDelta<String> delta : patch.getDeltas().subList(1, patch.getDeltas().size())) {
                System.out.println(delta);
            }
            return patch.getDeltas().subList(1, patch.getDeltas().size()).isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    public boolean compare(String filename1, String filename2) {
        //对比文件
        try {
            List<String> original = Files.readAllLines(new File(filename1).toPath());
            List<String> revised = Files.readAllLines(new File(filename2).toPath());
            Patch<String> patch = DiffUtils.diff(original, revised);
            for (AbstractDelta<String> delta : patch.getDeltas()) {
                System.out.println(delta);
            }
            return patch.getDeltas().isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void generateDiffFile(String filename1, String filename2, String output) {// filename1:output/2022/A/testfile1.txt
        List<String> diffString = DiffHandleUtils.diffString(filename1, filename2);// data/2022/testfiles-only/A/testfile1.txt
        createFile(output);
        DiffHandleUtils.generateDiffHtml(diffString, output);
    }

    public void createFile(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

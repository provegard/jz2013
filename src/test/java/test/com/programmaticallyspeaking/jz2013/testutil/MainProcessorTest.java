package test.com.programmaticallyspeaking.jz2013.testutil;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import javax.annotation.processing.Processor;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.programmaticallyspeaking.jz2013.MainProcessor;

public class MainProcessorTest extends AbstractAnnotationProcessorTest {

	private File outputPath;

	@Override
	protected Collection<? extends Processor> getProcessors() {
		return Collections.singleton(new MainProcessor());
	}

	@Override
	protected File getClassOutputPath() {
		return outputPath;
	}

	@BeforeMethod
	public void createOutputDir() {
		File tempDir = new File(System.getProperty("java.io.tmpdir"));
		outputPath = new File(tempDir, UUID.randomUUID().toString());
		outputPath.mkdirs();
	}

	@AfterMethod(alwaysRun = true)
	public void removeOutputDir() {
		deleteTree(getClassOutputPath());
	}

	private void deleteTree(File f) {
		if (!f.isFile()) {
			for (File sf : f.listFiles()) {
				deleteTree(sf);
			}
		}
		f.delete();
	}

}

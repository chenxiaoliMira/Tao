import com.ituotu.tao_core.cache.TaoCache;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Starter {

    public static void main(String [] args){
        ApplicationContext appContext = new FileSystemXmlApplicationContext("src/main/resources/applicationContext.xml");
        TaoCache.appContext = appContext;
    }
}

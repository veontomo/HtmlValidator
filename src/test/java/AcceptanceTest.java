import com.veontomo.htmlvalidator.GUI;
import com.veontomo.htmlvalidator.Views.MainView;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasChildren;

/**
 * Test suite for  GUI of the app.
 */
public class AcceptanceTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws Exception {
//        Scene scene = new Scene(MainView(stage).getScene(), 800, 600);

        stage.setScene(new MainView(stage).getScene());
        stage.show();
    }

    @Test
    public void clickOnMenuSelect() {
        // given:
        clickOn("#menuFile");
        verifyThat("#menuSelect", Node::isDisable);
//        rightClickOn("#desktop").moveTo("New").clickOn("Text Document");
//        write("myTextfile.txt").push(KeyCode.ENTER);


        // then:
//        verifyThat("#desktop", hasChildren(0, ".file"));
    }
}

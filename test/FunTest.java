import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FunTest {

    @Test
    public void forFun() {
        int i  = 5;
        assertThat(i).isEqualTo(5);
    }
}

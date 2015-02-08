package somepackage;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;

@ManagedBean
public class AutoCompleteView {

  private String txt6;

  public List<String> completeText(String query) {
    List<String> results = new ArrayList<String>();
    for (int i = 0; i < 10; i++) {
      results.add(query + i);
    }

    return results;
  }

  public String getTxt6() {
    System.out.println(InitServelet.testString);
    return txt6;
  }

  public void setTxt6(String txt6) {
    this.txt6 = txt6;
  }
}
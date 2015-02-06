package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class ToolConstants {
  
  public static enum KINDOFMEAL {
    Undefined, FullMeal, MainCourse, SideDish, Sauce, Dessert;
    
    private static final ResourceBundle languageBundle = ResourceBundle.getBundle("KindOfMeal");
    
    public static KINDOFMEAL intToMeal(Integer value) {
      switch (value) {
        case 0:
          return Undefined;
        case 1:
          return FullMeal;
        case 2:
          return MainCourse;
        case 3:
          return SideDish;
        case 4:
          return Sauce;
        case 5:
          return Dessert;
        default:
          return Undefined;
      }
    }

    public static Integer mealToInt(KINDOFMEAL kind) {
      switch (kind) {
        case Undefined:
          return 0;
        case FullMeal:
          return 1;
        case MainCourse:
          return 2;
        case SideDish:
          return 3;
        case Sauce:
          return 4;
        case Dessert:
          return 5;
        default:
          return -1;
      }
    }
    
    public String toString(){
      return languageBundle.getString(name());
    }
  };
  
  public static String getDateString(Date date, boolean simple){
    SimpleDateFormat dateFormat = new SimpleDateFormat();
    if(simple){
      dateFormat.applyPattern("yyyy-MM-dd");
    }
    else {
      dateFormat.applyPattern("EEEE, dd.MMM.yy");
    }
    String dateString = dateFormat.format(date).toString();
    return dateString;
  }
}

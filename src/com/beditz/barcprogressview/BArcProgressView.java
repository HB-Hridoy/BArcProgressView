package com.beditz.barcprogressview;

import android.graphics.Typeface;
import android.view.ViewGroup;
import android.view.animation.*;
import android.widget.LinearLayout;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.util.YailList;

import com.beditz.barcprogressview.lib.ArcProgressStackViewLibrary;


import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

public class BArcProgressView extends AndroidNonvisibleComponent {
  private Context context;
  private ComponentContainer container;
  private HashMap<String, ArcProgressStackViewLibrary> arcViews=new HashMap<>();
  private HashMap<String, LinearLayout> linearLayoutHashMap=new HashMap<>();
  private HashMap<String, ViewGroup> viewGroupHashMap=new HashMap<>();

  public BArcProgressView(ComponentContainer container) {
    super(container.$form());
    this.container = container;
    this.context = container.$context();
  }

  //Properties
  @SimpleProperty(description = "Returns TechniqueLinear to animation technique")
  public String TechniqueLinear(){
    return "Linear";
  }

  @SimpleProperty(description = "Returns TechniqueAccelerate to animation technique")
  public String TechniqueAccelerate(){
    return "Accelerate";
  }

  @SimpleProperty(description = "Returns TechniqueDecelerate to animation technique")
  public String TechniqueDecelerate(){
    return "Decelerate";
  }

  @SimpleProperty(description = "Returns TechniqueBounce to animation technique")
  public String TechniqueBounce(){
    return "Bounce";
  }

  @SimpleProperty(description = "Returns TechniqueOvershoot to animation technique")
  public String TechniqueOvershoot(){
    return "Overshoot";
  }

  @SimpleProperty(description = "Returns TechniqueAnticipate to animation technique")
  public String TechniqueAnticipate(){
    return "Anticipate";
  }

  @SimpleProperty(description = "Returns TechniqueAccelerateDecelerate to animation technique")
  public String TechniqueAccelerateDecelerate(){
    return "AccelerateDecelerate";
  }

  @SimpleProperty(description = "Returns TechniqueAnticipateOvershoot to animation technique")
  public String TechniqueAnticipateOvershoot(){
    return "AnticipateOvershoot";
  }

  @SimpleProperty(description = "Returns indicator orientation VERTICAL")
  public String IndicatorOrientationVertical (){
    return "VERTICAL";
  }
  @SimpleProperty(description = "Returns indicator orientation HORIZONTAL")
  public String IndicatorOrientationHorizontal (){
    return "HORIZONTAL";
  }

  @SimpleProperty(description = "Returns Default Sweep Angle")
  public int DefaultSweepAngle (){
    return 360;
  }

  @SimpleProperty(description = "Returns Default Start Angle")
  public int DefaultStartAngle (){
    return 270;
  }

  @SimpleProperty(description = "Returns Default Width FractionL")
  public float DefaultWidthFraction (){
    return Float.parseFloat("0.35");
  }

  @SimpleProperty(description = "Returns Default Progress Model Offset")
  public int DefaultProgressModelOffset (){
    return 5;
  }

  //Events
  @SimpleEvent(description = "Fires when an error occurred")
  public void ErrorOccurred (String error, String errorFrom){
    EventDispatcher.dispatchEvent(this, "ErrorOccurred", error, errorFrom);
  }


  // Functions
  @SimpleFunction(description = "Creates Round Progress Arc View. Parameter id needs to be unique")
  public void CreateRoundProgressArc(
          final String id,
          final AndroidViewComponent in,
          boolean roundedCorners,
          YailList progressViews
          ) {

    if (!idExists(id)) {
      ViewGroup viewGroup = (ViewGroup) in.getView();
      if (!viewGroupHashMap.containsValue(viewGroup)){

        ArcProgressStackViewLibrary apsv = new ArcProgressStackViewLibrary(context);
        apsv.setIsRounded(roundedCorners);
        apsv.setIsShadowed(false);


        final ArrayList<ArcProgressStackViewLibrary.Model> models = new ArrayList<>();
   /*
   models.add(new ArcProgressStackViewLibrary.Model("Circle", 25, mBgColor, mStartColor));
    models.add(new ArcProgressStackViewLibrary.Model("Progress", 50, mBgColor, mStartColor));
    models.add(new ArcProgressStackViewLibrary.Model("Stack", 75, mBgColor, mStartColor));
    */

        String[] mProgressViews = progressViews.toStringArray();
        for (int i = 0; i < mProgressViews.length; i++) {
          String[] mProgressProperties = mProgressViews[i].split("-/-");
          models.add(new ArcProgressStackViewLibrary.Model(
                          mProgressProperties[0],
                          Integer.parseInt(mProgressProperties[1]),
                          Integer.parseInt(mProgressProperties[2]),
                          Integer.parseInt(mProgressProperties[3])
                  )
          );
        }

        apsv.setModels(models);

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.addView(apsv);

        viewGroup.addView(linearLayout);
        linearLayoutHashMap.put(id, linearLayout);
        viewGroupHashMap.put(id, viewGroup);
        arcViews.put(id, apsv);
      }else {
        ErrorOccurred("Layout is already registered", "CreateRoundProgressArc");
      }

    }else {
      ErrorOccurred("ID already exist. Please provide a unique id", "CreateRoundProgressArc");
    }


  }

  @SimpleFunction(description = "A method to set progress model properties")
  public String ProgressProperties (
              String title,
              int progress,
              int foregroundColor,
              int backgroundColor
            ){

    return title + "-/-" + progress + "-/-" + backgroundColor + "-/-" + foregroundColor;

  }

  @SimpleFunction(description = "")
  public void RemoveCreateRoundProgressArc (String id){

    if (idExists(id)){
      LinearLayout linearLayout = linearLayoutHashMap.get(id);
      ViewGroup viewGroup = viewGroupHashMap.get(id);

      viewGroup.removeView(linearLayout);

      linearLayoutHashMap.remove(id);
      viewGroupHashMap.remove(id);
      arcViews.remove(id);

    }else {
      ErrorOccurred("id does not exist", "RemoveCreateRoundProgressArc");
    }

  }

  @SimpleFunction(description = "")
  public void BackgroundEnable (String id, boolean enable){

    if (idExists(id)){
      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      apsv.setModelBgEnabled(enable);

      AnimateProgress(id, 0, "Linear");

      arcViews.replace(id,apsv);
    }else {
      ErrorOccurred("id does not exist", "BackgroundEnable");
    }

  }

  @SimpleFunction(description = "")
  public boolean IsBackgroundEnabled(String id){

    if (idExists(id)) {
      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      return apsv.isModelBgEnabled();
    }else {
      ErrorOccurred("id does not exist", "IsBackgroundEnabled");
      return true;
    }


  }

  @SimpleFunction(description = "")
  public void SetSweepAngle (String id, final float sweepAngle){

    if (idExists(id)){
      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      apsv.setSweepAngle(sweepAngle);

      AnimateProgress(id, 0, "Linear");

      arcViews.replace(id,apsv);
    }else {
      ErrorOccurred("id does not exist", "SetSweepAngle");
    }

  }

  @SimpleFunction(description = "")
  public float GetSweepAngle(String id){

    if (idExists(id)) {
      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      return apsv.getSweepAngle();
    }else {
      ErrorOccurred("id does not exist", "GetSweepAngle");
      return 0;
    }


  }

  @SimpleFunction(description = "")
  public void SetStartAngle (String id, final float startAngle){

    if (idExists(id)){
      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      apsv.setStartAngle(startAngle);

      AnimateProgress(id, 0, "Linear");

      arcViews.replace(id,apsv);
    }else {
      ErrorOccurred("id does not exist", "setStartAngle");
    }

  }

  @SimpleFunction(description = "")
  public float GetStartAngle(String id){

    if (idExists(id)) {
      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      return apsv.getStartAngle();
    }else {
      ErrorOccurred("id does not exist", "GetStartAngle");
      return 0;
    }


  }

  @SimpleFunction(description = "")
  public void SetShadow (
          String id,
          int shadowColor,
          float shadowDistance,
          float shadowAngle,
          float shadowRadius

  ){

    if (idExists(id)){
      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      apsv.setIsShadowed(true);
      apsv.setShadowColor(shadowColor);
      apsv.setShadowDistance(shadowDistance);
      apsv.setShadowAngle(shadowAngle);
      apsv.setShadowRadius(shadowRadius);

      AnimateProgress(id, 0, "Linear");

      arcViews.replace(id,apsv);
    }else {
      ErrorOccurred("id does not exist", "SetShadow");
    }

  }

  @SimpleFunction(description = "")
  public boolean IsShadowed(String id){

    if (idExists(id)) {
      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      return apsv.isShadowed();
    }else {
      ErrorOccurred("id does not exist", "IsShadowed");
      return false;
    }


  }

  @SimpleFunction(description = "")
  public void DisableShadow(String id){
    if (idExists(id)){
      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      apsv.setIsShadowed(false);
      apsv.setShadowRadius(0);
      apsv.setShadowDistance(0);

      AnimateProgress(id, 0, "Linear");

      arcViews.replace(id,apsv);
    }else {
      ErrorOccurred("id does not exist", "DisableShadow");
    }
  }

  @SimpleFunction(description = "")
  public void SetTextColor (String id, int textColor){

    if (idExists(id)){
      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      apsv.setTextColor(textColor);

      AnimateProgress(id, 0, "Linear");

      arcViews.replace(id,apsv);
    }else {
      ErrorOccurred("id does not exist", "SetTextColor");
    }

  }

  @SimpleFunction(description = "")
  public int GetTextColor(String id){

    if (idExists(id)) {
      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      return apsv.getTextColor();
    }else {
      ErrorOccurred("id does not exist", "GetTextColor");
      return -1;
    }


  }

  @SimpleFunction(description = "")
  public void SetTypeface (String id, String typefacePath){

    if (idExists(id)){

      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      apsv.setTypeface(Typeface.createFromFile(typefacePath));

      AnimateProgress(id, 0, "Linear");

      arcViews.replace(id,apsv);
    }else {
      ErrorOccurred("id does not exist", "SetTypeface");
    }

  }


  @SimpleFunction(description = "")
  public void AnimateProgress (String id, long duration, String technique){

    if (idExists(id)){

      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      apsv.setIsAnimated(true);
      apsv.setAnimationDuration(duration);
      if ("Linear".equals(technique)) {
        apsv.setInterpolator(new LinearInterpolator());
      } else if ("Accelerate".equals(technique)) {
        apsv.setInterpolator(new AccelerateInterpolator());
      } else if ("Decelerate".equals(technique)) {
        apsv.setInterpolator(new DecelerateInterpolator());
      } else if ("Bounce".equals(technique)) {
        apsv.setInterpolator(new BounceInterpolator());
      } else if ("Overshoot".equals(technique)) {
        apsv.setInterpolator(new OvershootInterpolator());
      } else if ("Anticipate".equals(technique)) {
        apsv.setInterpolator(new AnticipateInterpolator());
      } else if ("AccelerateDecelerate".equals(technique)) {
        apsv.setInterpolator(new AccelerateDecelerateInterpolator());
      } else if ("AnticipateOvershoot".equals(technique)) {
        apsv.setInterpolator(new AnticipateOvershootInterpolator());
      }
      apsv.animateProgress();


      arcViews.replace(id,apsv);
    }else {
      ErrorOccurred("id does not exist", "AnimateProgress");
    }

  }

  @SimpleFunction(description = "")
  public void Draggable(String id, boolean draggable){

    if (idExists(id)){

      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      apsv.setIsDragged(draggable);

      AnimateProgress(id, 0, "Linear");

      arcViews.replace(id,apsv);
    }else {
      ErrorOccurred("id does not exist", "Draggable");
    }

  }

  @SimpleFunction(description = "")
  public boolean IsDraggable(String id){

    if (idExists(id)) {
      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      return apsv.isDragged();
    }else {
      ErrorOccurred("id does not exist", "IsDraggable");
      return false;
    }


  }

  @SimpleFunction(description = "")
  public void Level(String id, boolean leveled){

    if (idExists(id)){

      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      apsv.setIsDragged(leveled);

      AnimateProgress(id, 0, "Linear");

      arcViews.replace(id,apsv);
    }else {
      ErrorOccurred("id does not exist", "Level");
    }

  }

  @SimpleFunction(description = "")
  public boolean IsLeveled(String id){

    if (idExists(id)) {
      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      return apsv.isLeveled();
    }else {
      ErrorOccurred("id does not exist", "IsLeveled");
      return false;
    }


  }

  @SimpleFunction(description = "")
  public void WidthFraction(String id, float widthFraction){

    if (idExists(id)){

      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      apsv.setDrawWidthFraction(widthFraction);

      AnimateProgress(id, 0, "Linear");

      arcViews.replace(id,apsv);
    }else {
      ErrorOccurred("id does not exist", "DrawWidthFraction");
    }

  }

  @SimpleFunction(description = "")
  public float GetWidthFraction(String id){

    if (idExists(id)) {
      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      return apsv.getDrawWidthFraction();
    }else {
      ErrorOccurred("id does not exist", "GetWidthFraction");
      return 0;
    }


  }

  @SimpleFunction(description = "")
  public void ProgressModelOffset(String id, float progressModelOffset){

    if (idExists(id)){

      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      apsv.setProgressModelOffset(progressModelOffset);

      AnimateProgress(id, 0, "Linear");


      arcViews.replace(id,apsv);
    }else {
      ErrorOccurred("id does not exist", "ProgressModelOffset");
    }

  }

  @SimpleFunction(description = "")
  public float GetProgressModelOffset(String id){

    if (idExists(id)) {
      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      return apsv.getProgressModelOffset();
    }else {
      ErrorOccurred("id does not exist", "GetProgressModelOffset");
      return 0;
    }


  }

  @SimpleFunction(description = "")
  public void IndicatorOrientation(String id, String indicatorOrientation){

    if (idExists(id)){

      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      if (indicatorOrientation.equals("VERTICAL")){
        apsv.setIndicatorOrientation(ArcProgressStackViewLibrary.IndicatorOrientation.VERTICAL);
      }else if (indicatorOrientation.equals("HORIZONTAL")){
        apsv.setIndicatorOrientation(ArcProgressStackViewLibrary.IndicatorOrientation.HORIZONTAL);
      }else{
        ErrorOccurred("Invalid orientation type", "IndicatorOrientation");
      }

      AnimateProgress(id, 0, "Linear");

      arcViews.replace(id,apsv);
    }else {
      ErrorOccurred("id does not exist", "IndicatorOrientation");
    }

  }

  @SimpleFunction(description = "")
  public String GetIndicatorOrientation(String id){

    if (idExists(id)) {
      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      return apsv.getIndicatorOrientation().toString();
    }else {
      ErrorOccurred("id does not exist", "GetIndicatorOrientation");
      return "null";
    }


  }

  @SimpleFunction(description = "")
  public void RoundedCorners(String id, boolean roundedCorners){

    if (idExists(id)){

      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      apsv.setIsRounded(roundedCorners);

      AnimateProgress(id, 0, "Linear");


      arcViews.replace(id,apsv);
    }else {
      ErrorOccurred("id does not exist", "RoundedCorners");
    }

  }

  @SimpleFunction(description = "")
  public boolean IsRoundedCorners(String id) {

    if (idExists(id)) {
      ArcProgressStackViewLibrary apsv = arcViews.get(id);
      return apsv.isRounded();
    } else {
      ErrorOccurred("id does not exist", "IsRoundedCorners");
      return false;
    }
  }


  @SimpleFunction(description = "")
  public boolean idExists(String id) {
    return arcViews.containsKey(id);
  }

}

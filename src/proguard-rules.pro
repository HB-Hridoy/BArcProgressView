# Add any ProGuard configurations specific to this
# extension here.

-keep public class com.beditz.barcprogressview.BArcProgressView {
    public *;
 }
-keeppackagenames gnu.kawa**, gnu.expr**

-optimizationpasses 4
-allowaccessmodification
-mergeinterfacesaggressively

-repackageclasses 'com/beditz/barcprogressview/repack'
-flattenpackagehierarchy
-dontpreverify

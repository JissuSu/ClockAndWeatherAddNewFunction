package mobi.infolife.launcher2;
interface IMyAppWidgetManagerService {
  int[] getAppWidgetIds(in ComponentName cn);
  void updateAppWidgetIds(in int[] appWidgetIds, in RemoteViews views);
  void updateAppWidgetProvider(in ComponentName provider, in RemoteViews views);
  boolean isRunning();
}
/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\EclipseProject\\MyClock\\src\\mobi\\infolife\\launcher2\\IMyAppWidgetManagerService.aidl
 */
package mobi.infolife.launcher2;
public interface IMyAppWidgetManagerService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements mobi.infolife.launcher2.IMyAppWidgetManagerService
{
private static final java.lang.String DESCRIPTOR = "mobi.infolife.launcher2.IMyAppWidgetManagerService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an mobi.infolife.launcher2.IMyAppWidgetManagerService interface,
 * generating a proxy if needed.
 */
public static mobi.infolife.launcher2.IMyAppWidgetManagerService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof mobi.infolife.launcher2.IMyAppWidgetManagerService))) {
return ((mobi.infolife.launcher2.IMyAppWidgetManagerService)iin);
}
return new mobi.infolife.launcher2.IMyAppWidgetManagerService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getAppWidgetIds:
{
data.enforceInterface(DESCRIPTOR);
android.content.ComponentName _arg0;
if ((0!=data.readInt())) {
_arg0 = android.content.ComponentName.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
int[] _result = this.getAppWidgetIds(_arg0);
reply.writeNoException();
reply.writeIntArray(_result);
return true;
}
case TRANSACTION_updateAppWidgetIds:
{
data.enforceInterface(DESCRIPTOR);
int[] _arg0;
_arg0 = data.createIntArray();
android.widget.RemoteViews _arg1;
if ((0!=data.readInt())) {
_arg1 = android.widget.RemoteViews.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
this.updateAppWidgetIds(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_updateAppWidgetProvider:
{
data.enforceInterface(DESCRIPTOR);
android.content.ComponentName _arg0;
if ((0!=data.readInt())) {
_arg0 = android.content.ComponentName.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
android.widget.RemoteViews _arg1;
if ((0!=data.readInt())) {
_arg1 = android.widget.RemoteViews.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
this.updateAppWidgetProvider(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_isRunning:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isRunning();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements mobi.infolife.launcher2.IMyAppWidgetManagerService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public int[] getAppWidgetIds(android.content.ComponentName cn) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((cn!=null)) {
_data.writeInt(1);
cn.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_getAppWidgetIds, _data, _reply, 0);
_reply.readException();
_result = _reply.createIntArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void updateAppWidgetIds(int[] appWidgetIds, android.widget.RemoteViews views) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeIntArray(appWidgetIds);
if ((views!=null)) {
_data.writeInt(1);
views.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_updateAppWidgetIds, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void updateAppWidgetProvider(android.content.ComponentName provider, android.widget.RemoteViews views) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((provider!=null)) {
_data.writeInt(1);
provider.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
if ((views!=null)) {
_data.writeInt(1);
views.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_updateAppWidgetProvider, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean isRunning() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isRunning, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getAppWidgetIds = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_updateAppWidgetIds = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_updateAppWidgetProvider = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_isRunning = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
public int[] getAppWidgetIds(android.content.ComponentName cn) throws android.os.RemoteException;
public void updateAppWidgetIds(int[] appWidgetIds, android.widget.RemoteViews views) throws android.os.RemoteException;
public void updateAppWidgetProvider(android.content.ComponentName provider, android.widget.RemoteViews views) throws android.os.RemoteException;
public boolean isRunning() throws android.os.RemoteException;
}

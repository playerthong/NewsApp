package newsapi.org.android;


import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class BaseActivity extends AppCompatActivity {
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposables!=null && !disposables.isDisposed()) disposables.dispose();
    }

    /**
     * processTask is function help mobile process background task as async task
     * @param backgroundTask is Observable and run in background with long task
     * @param disposableObserver handle UI like post excute of async task
     */
    public void processTask(Observable backgroundTask, DisposableObserver disposableObserver){
        Disposable disposable = (Disposable) backgroundTask
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(disposableObserver);
        disposables.add(disposable);
    }
}

package com.lalaland.ecommerce.helpers;

import androidx.appcompat.widget.SearchView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class RxSearchObservable {

    public static Observable<String> fromView(SearchView searchView) {

        final Observable<String> subject = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        emitter.onComplete();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String text) {
                        emitter.onNext(text);
                        return true;
                    }
                });
            }
        });


        return subject;
    }
}
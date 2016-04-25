package com.khasang.forecast.adapters.etc;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.widget.FrameLayout;
import com.khasang.forecast.Logger;
import com.khasang.forecast.R;

/**
 * Слушатель скролов по RecyclerView в {@link com.khasang.forecast.activities.WeatherActivity}
 * Управляет появлением верхнего и нижнего Layout, показывающих текущую погоду и график температуры
 * соответственно.
 *
 * После исчезновения текущей погоды, начинает появляться график.
 * После исчезновения графика, появляется текущая погода.
 *
 * Текущая погода "уезжает вверх".
 * У графика изменяется высота .
 *
 */
public class WeatherScrollListener extends RecyclerView.OnScrollListener {

    private final String TAG = this.getClass().getSimpleName();

    private float chartHeight;
    private int appBarHeight;
    private boolean appBarVisible = true;

    private FloatingActionButton fab;
    private FrameLayout chatLayout;
    private AppBarLayout appBarLayout;

    public WeatherScrollListener(Context context, FloatingActionButton fab, FrameLayout chatLayout,
        AppBarLayout appBarLayout) {

        this.fab = fab;
        this.chatLayout = chatLayout;
        this.appBarLayout = appBarLayout;

        // Максимальная высота графика
        this.chartHeight = context.getResources().getDimension(R.dimen.chart_height);

        this.appBarHeight = appBarLayout.getLayoutParams().height;

        // Убираем дефотное поведение - исчезновение FAB после исчезновения родителя
        CoordinatorLayout.LayoutParams fabLayoutParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        fabLayoutParams.setBehavior(null);
    }

    @Override public void onScrolled(RecyclerView recyclerView, int dx, final int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy > 0) {
            // Движении пальцем вверх
            scrollUp(dy);
        } else {
            // Движении пальцем вниз
            scrollDown(dy);
        }
    }

    private void scrollUp(int dy) {
        CoordinatorLayout.LayoutParams fabLayoutParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        if (fabLayoutParams.getAnchorId() == R.id.appbar) {
            // FAB на текущей погоде
            scrollAppBarUp(dy, fabLayoutParams);
        } else {
            // FAB на графике
            scrollChartUp(dy);
        }
    }

    private void scrollAppBarUp(int dy, CoordinatorLayout.LayoutParams fabLayoutParams) {
        if (appBarVisible) {
            // FAB на текущей погоде - сдвигаем Layout вверх

            CoordinatorLayout.LayoutParams appBarLayoutParams =
                (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();

            int newTopMargin = appBarLayoutParams.topMargin - dy;
            if (-newTopMargin > appBarHeight) {
                // Устанавливаем margin ровно высоте AppBarLayout, на случай если
                // пользователь быстро скрольнул
                newTopMargin = -appBarHeight;
                appBarVisible = false;
            }
            appBarLayoutParams.topMargin = newTopMargin;
            appBarLayout.requestLayout();
        } else {
            Logger.println(TAG, "Текущая погода скрылась - перекидываем FAB на Layout графика");
            fabLayoutParams.setAnchorId(R.id.chart_layout);
            fabLayoutParams.anchorGravity = Gravity.TOP | Gravity.END;
        }
    }

    private void scrollChartUp(int dy) {
        int newHeight = chatLayout.getLayoutParams().height + dy;
        if (newHeight > chartHeight) {
            // Устанавливаем высоту ровно на необходимую высоту, на случай если
            // пользователь быстро скрольнул
            newHeight = (int) chartHeight;
        }
        chatLayout.getLayoutParams().height = newHeight;
        chatLayout.requestLayout();
    }

    private void scrollDown(int dy) {
        CoordinatorLayout.LayoutParams fabLayoutParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        if (fabLayoutParams.getAnchorId() == R.id.chart_layout) {
            // FAB на графике
            scrollChartDown(dy, fabLayoutParams);
        } else {
            // FAB на текущей погоде
            scrollAppBarDown(dy);
        }
    }

    private void scrollChartDown(int dy, CoordinatorLayout.LayoutParams fabLayoutParams) {
        if (!appBarVisible) {
            int newHeight = chatLayout.getLayoutParams().height + dy;
            if (newHeight <= 0) {
                // Устанавливаем высоту равную 0, на случай если пользователь быстро скрольнул
                newHeight = 0;
                appBarVisible = true;
            }
            chatLayout.getLayoutParams().height = newHeight;
            chatLayout.requestLayout();
        } else {
            Logger.println(TAG, "График скрылся - перекидываем FAB на Layout текущей погоды");
            fabLayoutParams.setAnchorId(R.id.appbar);
            fabLayoutParams.anchorGravity = Gravity.BOTTOM | Gravity.END;
        }
    }

    private void scrollAppBarDown(int dy) {
        CoordinatorLayout.LayoutParams appBarLayoutParams =
            (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();

        int newTopMargin = appBarLayoutParams.topMargin - dy;
        if (newTopMargin > 0) {
            // Устанавливаем margin равным 0, на случай если пользователь быстро скрольнул
            newTopMargin = 0;
        }
        appBarLayoutParams.topMargin = newTopMargin;
        chatLayout.requestLayout();
    }
}
package com.jiaying.mediatablet.net.state.RecoverState;

import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.stateswitch.CollectionState;
import com.jiaying.mediatablet.net.state.stateswitch.EndState;
import com.jiaying.mediatablet.net.state.stateswitch.TabletStateContext;
import com.jiaying.mediatablet.net.state.stateswitch.WaitingForAuthState;
import com.jiaying.mediatablet.net.state.stateswitch.WaitingForCheckOverState;
import com.jiaying.mediatablet.net.state.stateswitch.WaitingForCompressionState;
import com.jiaying.mediatablet.net.state.stateswitch.WaitingForDonorState;
import com.jiaying.mediatablet.net.state.stateswitch.WaitingForPunctureState;

import com.jiaying.mediatablet.net.state.stateswitch.WaitingForSerResState;

import com.jiaying.mediatablet.net.state.stateswitch.WaitingForStartState;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;

/**
 * Created by hipil on 2016/4/3.
 */
public class RecoverState {

    public void recover(RecordState recordState, ObservableZXDCSignalListenerThread observableZXDCSignalListenerThread, TabletStateContext tabletStateContext) {

        recordState.retrieve();
        String state = recordState.getState();
        if (state == null) {
            //设置当前的状态

            tabletStateContext.setCurrentState(EndState.getInstance());
            //使当前页面跳转到该状态下的样子
            tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.CHECKSTART);
        } else if (StateIndex.WAITINGFORCHECKOVER.equals(state)) {
            //检查电量状态的恢复方式
            //设置当前的状态
            tabletStateContext.setCurrentState(EndState.getInstance());
            //使当前页面跳转到该状态下的样子
            tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.CHECKSTART);
        } else if (StateIndex.WAITINGFORDONOR.equals(state)) {
            //等待献浆员状态
            //设置当前的状态
            tabletStateContext.setCurrentState(WaitingForCheckOverState.getInstance());
            //使当前页面跳转到该状态下的样子
            tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.CHECKOVER);
        } else if (StateIndex.WAITINGFORAUTH.equals(state)) {
            //等待认证通过
            //设置当前的状态
            tabletStateContext.setCurrentState(WaitingForDonorState.getInstance());
            //使当前页面跳转到该状态下的样子
            tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.CONFIRM);
        } else if (StateIndex.WAITINGFORCOMPRESSION.equals(state)) {
            //等待加压状态
            //设置当前的状态
            tabletStateContext.setCurrentState(WaitingForSerResState.getInstance());
            //使当前页面跳转到该状态下的样子
            tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.SERAUTHRES);
        } else if (StateIndex.WAITINGFORPUNCTURE.equals(state)) {
            //设置当前的状态
            tabletStateContext.setCurrentState(WaitingForCompressionState.getInstance());
            //使当前页面跳转到该状态下的样子
            tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.COMPRESSINON);
        } else if (StateIndex.WAITINGFORSTART.equals(state)) {
            //设置当前的状态
            tabletStateContext.setCurrentState(WaitingForPunctureState.getInstance());
            //使当前页面跳转到该状态下的样子
            tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.PUNCTURE);
        } else if (StateIndex.COLLECTION.equals(state)) {
            //设置当前的状态
            tabletStateContext.setCurrentState(WaitingForStartState.getInstance());
            //使当前页面跳转到该状态下的样子
            tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.START);
        } else if (StateIndex.END.equals(state)) {
            //设置当前的状态
            tabletStateContext.setCurrentState(CollectionState.getInstance());
            //使当前页面跳转到该状态下的样子
            tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.END);
        }
    }

}

import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import App from "./App";
import * as serviceWorkerRegistration from "./serviceWorkerRegistration";
// import reportWebVitals from './reportWebVitals';

import { store } from "./redux/store";
import { Provider } from "react-redux";
import { setupFirebaseMessaging } from "./utils/notificationUtils";


window.Kakao.init(process.env.REACT_APP_JAVASCRIPT_KEY);
window.Kakao.isInitialized();

const root = ReactDOM.createRoot(document.getElementById("root") as HTMLElement);
root.render(
  <Provider store={store}>
    <App />
  </Provider>
);

serviceWorkerRegistration.register();

// 포그라운드 fcm 알림 수신 
setupFirebaseMessaging();

// reportWebVitals();

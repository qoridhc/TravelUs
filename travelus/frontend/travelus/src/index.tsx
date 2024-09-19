import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import App from "./App";
import * as serviceWorkerRegistration from "./serviceWorkerRegistration";
// import reportWebVitals from './reportWebVitals';

import { store } from "./redux/store";
import { Provider } from "react-redux";

window.Kakao.init(process.env.REACT_APP_JAVASCRIPT_KEY);
window.Kakao.isInitialized();

const root = ReactDOM.createRoot(document.getElementById("root") as HTMLElement);
root.render(
  <Provider store={store}>
    <App />
  </Provider>
);

serviceWorkerRegistration.register();

// reportWebVitals();

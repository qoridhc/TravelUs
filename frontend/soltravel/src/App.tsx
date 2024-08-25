import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import MainPage from "../src/pages/MainPage";
import Login from "./pages/user/Login";
import SignUp from "./pages/user/SignUp";
import MyAccount from "./pages/viewaccount/MyAccount";
import GeneralAccount from "./pages/viewaccount/GeneralAccount";
import ForeignAccount from "./pages/viewaccount/ForeignAccount";
import ViewAccount from "./pages/viewaccount/ViewAccount";
import Header from "./components/common/Header";
import Footer from "./components/common/Footer";
import AccountCreate from "./pages/account/AccountCreate";

function App() {
  return (
    <div>
      <BrowserRouter>
        <Routes>
          <Route
            path="/*"
            element={
              <>
                <Header />
                <div>
                  <Routes>
                    <Route path="/" element={<MainPage />} />
                  </Routes>
                </div>
                <Footer />
              </>
            }
          />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<SignUp />} />
          <Route path="/accountcreate" element={<AccountCreate />}></Route>
          <Route path="/myaccount" element={<MyAccount />}></Route>
          <Route path="/generalaccount" element={<GeneralAccount />}></Route>
          <Route path="/foreignaccount" element={<ForeignAccount />}></Route>
          <Route path="/account" element={<ViewAccount />}></Route>
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;

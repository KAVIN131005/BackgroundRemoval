import { Toaster } from "react-hot-toast";
import Footer from "./components/Footer.jsx";
import Menubar from "./components/Menubar.jsx";
import Home from "./pages/Home.jsx";
import Result from "./pages/Result.jsx"; // ✅ fixed import
import { Route, Routes } from "react-router-dom";
import { SignedIn, SignedOut, RedirectToSignIn } from "@clerk/clerk-react";

const App = () => {
  return (
    <div>
      <Menubar />
      <Toaster />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route
          path="/result"
          element={
            <>
              <SignedIn>
                <Result />
              </SignedIn>
              <SignedOut>
                <RedirectToSignIn />
              </SignedOut>
            </>
          }
        />
      </Routes>
      <Footer />
    </div>
  );
};

export default App;

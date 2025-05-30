import { createContext, useContext, useState, useEffect, useCallback } from "react";
import { useAuth, useClerk, useUser } from "@clerk/clerk-react";
import axios from "axios";
import toast from "react-hot-toast";
import { useNavigate } from "react-router-dom";

// Create the context
// eslint-disable-next-line react-refresh/only-export-components
export const AppContext = createContext();

const AppContextProvider = (props) => {
  const backendUrl = import.meta.env.VITE_BACKEND_URL;
  const [credits, setCredits] = useState(0);
  const [image, setImage] = useState(false);
  const [resultImage, setResultImage] = useState(false);

  const { getToken } = useAuth();
  const { isSignedIn } = useUser();
  const {openSignIn}=useClerk();
 const navigate=useNavigate();

  const loadUserCredits = useCallback(async () => {
    try {
      const token = await getToken();
      if (!token) {
        console.warn("Token not found. Skipping credit load.");
        return;
      }

      const response = await axios.get(`${backendUrl}/users/credits`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.data.success) {
        setCredits(response.data.data.credits);
        console.log("✅ Credits fetched:", response.data.data.credits);
      } else {
        toast.error("❌ Error loading credits");
        console.error("Response error:", response.data);
      }
    } catch (error) {
      toast.error("❌ Error loading credits");
      console.error("Load credits error:", error);
    }
  }
  , [backendUrl, getToken]);
 const removeBg = async (selectedImage) => {
  try {
    if (!isSignedIn) {
      return openSignIn();
    }

    setImage(selectedImage);
    setResultImage(false);
    navigate("/result");

    const token = await getToken();
    const formData = new FormData();
    selectedImage && formData.append("file", selectedImage);

    const { data } = await axios.post(
      `${backendUrl}/images/remove-background`,
      formData,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    // ✅ Ensure you're getting the actual base64 string
    const base64Image =
      typeof data === "string" ? data : data?.data; // adjust if backend returns wrapped response

    if (!base64Image) {
      throw new Error("No base64 image found in response");
    }

    setResultImage(`data:image/png;base64,${base64Image}`);
    setCredits((prev) => prev - 1);
  } catch (error) {
    console.log("Background removal error:", error);
    toast.error("Error while removing background image");
  }
};


  useEffect(() => {
    if (isSignedIn) {
      loadUserCredits();
    }
  }, [isSignedIn, loadUserCredits]);

  return (
    <AppContext.Provider
      value={{
        backendUrl,
        credits,
        setCredits,
        loadUserCredits,
        image,
        setImage,
        resultImage,
        setResultImage,
        removeBg
      }}
    >
      {props.children}
    </AppContext.Provider>
  );
};

export default AppContextProvider;

// ✅ Custom hook for using the context
// eslint-disable-next-line react-refresh/only-export-components
export const useContest = () => useContext(AppContext);

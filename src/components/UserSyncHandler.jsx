import React, { useState, useContext, useEffect } from "react";
import { useAuth, useUser } from "@clerk/clerk-react";
import { AppContext } from "../context/AppContext";
import { toast } from "react-toastify";
import axios from "axios";

const UserSyncHandler = () => {
  const { isLoaded, isSignedIn, getToken } = useAuth();
  const { user } = useUser();
  const [synced, setSynced] = useState(false);
  const { backendUrl ,loadUserCredits} = useContext(AppContext);3

  useEffect(() => {
    const saveUser = async () => {
      // Prevent multiple calls or running before auth/user is ready
      if (!isLoaded || !isSignedIn || synced || !user) {
        return;
      }

      try {
        const token = await getToken();
        if (!token) {
          console.warn("No auth token available");
          return;
        }

        // Safe access to nested properties
        const userData = {
          clerkId: user.id,
          email: user.primaryEmailAddress?.emailAddress || "",
          firstName: user.firstName || "",
          lastName: user.lastName || "",
        };

        // POST user data to backend with authorization header
       await axios.post(`${backendUrl}/users`, userData, {
  headers: { Authorization: `Bearer ${token}` }
});

        setSynced(true); // Avoid repeating the sync
       await loadUserCredits(); // Load user credits after sync
       
      } catch (error) {
        console.error("User sync failed", error);
        toast.error("User sync failed. Please try again.");
      }
    };

    saveUser();
  }, [isLoaded, isSignedIn, getToken, user, synced, backendUrl, loadUserCredits]);

  // No UI, this component only handles syncing user info
  return null;
};

export default UserSyncHandler;

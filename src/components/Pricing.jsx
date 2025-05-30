import { useContext } from "react";
import { plans } from "../assets/assets";
import { useAuth, useClerk } from "@clerk/clerk-react";
import { AppContext } from "../context/AppContext";

const Pricing = () => {
  const { isSignedIn, getToken } = useAuth();
  const { openSignIn } = useClerk();
  const { loadUserCredits, backendUrl } = useContext(AppContext);

  const handleOrder = async (planId) => {
    if (!isSignedIn) return openSignIn();

    try {
      const token = await getToken();
      if (!token) throw new Error("User token not found. Please log in again.");

      const res = await fetch(`${backendUrl}/orders`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ planId }),
      });

     

   if (!res.ok) {
  const errText = await res.text(); // read raw response
  throw new Error(`Order creation failed: ${errText || res.status}`);
}

const data = await res.json();

      const razorpayOrder = data.data;

      const options = {
        key: import.meta.env.VITE_RAZORPAY_KEY_ID,
        amount: razorpayOrder.amount,
        currency: "INR",
        name: "Photography Package",
        description: planId,
        order_id: razorpayOrder.id,
        handler: async (response) => {
          try {
            const verifyRes = await fetch(`${backendUrl}/orders/verify`, {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
              body: JSON.stringify({
                razorpay_order_id: response.razorpay_order_id,
                razorpay_payment_id: response.razorpay_payment_id,
                razorpay_signature: response.razorpay_signature,
                planId,
              }),
            });

            if (verifyRes.ok) {
              alert("✅ Payment successful!");
              loadUserCredits();
            } else {
              alert("❌ Payment verification failed.");
            }
          } catch (err) {
            console.error("Verification error:", err);
            alert("❌ An error occurred during verification.");
          }
        },
        theme: { color: "#7C3AED" },
      };

      const razorpay = new window.Razorpay(options);
      razorpay.open();

    } catch (error) {
      console.error("Order Error:", error);
      alert(`❌ ${error.message}`);
    }
  };

  // 🧠 You missed this return before:
  return (
    <section className="py-12 px-4 md:px-20 lg:px-32 bg-gray-100">
      <div className="max-w-7xl mx-auto">
        <header className="mb-16 text-center">
          <h2 className="text-4xl md:text-5xl font-extrabold text-gray-900 mb-4">
            Choose Your Perfect Package
          </h2>
          <p className="mx-auto text-gray-600 max-w-2xl">
            Select from our carefully curated photography packages designed to meet your specific needs and budget.
          </p>
        </header>

        <div className="grid gap-8 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3">
          {plans.map((plan) => (
            <div
              key={plan.id}
              className={`relative flex flex-col h-full 
                bg-[#1A1A1A] 
                ${plan.popular ? "ring-4 ring-purple-600 bg-opacity-90" : "bg-opacity-80"} 
                rounded-3xl overflow-hidden 
                transform transition-transform duration-300 hover:-translate-y-2 shadow-2xl`}
            >
              {plan.popular && (
                <span className="absolute top-4 left-1/2 -translate-x-1/2 bg-purple-600 text-white text-sm font-semibold px-4 py-1 rounded-full uppercase tracking-wide shadow-md">
                  Most Popular
                </span>
              )}

              <div className="px-8 pt-12 pb-6 text-center flex-shrink-0">
                <h3 className="text-2xl font-bold text-white mb-2">{plan.name}</h3>
                <p className="text-xl text-gray-400">{plan.credits}</p>
                <div className="mt-4">
                  <span className="text-5xl font-extrabold text-violet-400">
                    ₹{plan.price}
                  </span>
                </div>
              </div>

              <div className="border-t border-gray-700 mx-8"></div>

              <div className="px-8 py-6 mt-auto">
                <p className="text-gray-300 mb-6">{plan.description}</p>
                <button
                  className="w-full py-3 text-white font-semibold rounded-full bg-gradient-to-r from-purple-500 to-indigo-500 shadow-lg hover:from-purple-600 hover:to-indigo-600 transition-colors duration-300 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-purple-400"
                  onClick={() => handleOrder(plan.id)}
                >
                  Choose Plan
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default Pricing;

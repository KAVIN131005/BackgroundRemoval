import { useEffect, useState } from "react";
import { useContest } from "../context/AppContext";
import { useNavigate } from "react-router-dom";

const Result = () => {
  const { image, resultImage, setImage, setResultImage } = useContest();
  const [originalUrl, setOriginalUrl] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    if (image) {
      const objectUrl = URL.createObjectURL(image);
      setOriginalUrl(objectUrl);
      return () => URL.revokeObjectURL(objectUrl);
    }
  }, [image]);

  const handleReset = () => {
    setImage(false);
    setResultImage(false);
    navigate("/");
  };

  return (
    <div className="mx-4 my-3 lg:mx-44 mt-14 min-h-[75vh]">
      {/* image container */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8 justify-items-center">
        {/* Left Side - Original */}
        <div className="flex flex-col items-center">
          <p className="font-semibold text-gray-600 mb-2">Original</p>
          {originalUrl && (
            <img
              src={originalUrl}
              alt="original"
              className="rounded-md object-contain max-w-[300px] max-h-[300px] border"
            />
          )}
        </div>

        {/* Right Side - Background Removed */}
        <div className="flex flex-col items-center">
          <p className="font-semibold text-gray-600 mb-2">Background Removed</p>
          <div className="rounded-md border border-gray-300 bg-layer relative overflow-hidden flex items-center justify-center min-h-[320px] max-w-[320px]">
            {resultImage ? (
              <img
                src={resultImage}
                alt="result"
                className="object-contain max-w-[300px] max-h-[300px]"
              />
            ) : (
              image && (
                <div className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2">
                  <div className="border-4 border-indigo-600 rounded-full h-12 w-12 border-t-transparent animate-spin"></div>
                </div>
              )
            )}
          </div>
        </div>
      </div>

      {/* Buttons */}
      {resultImage && (
        <div className="flex justify-center items-center flex-wrap gap-4 mt-6">
          <button
            onClick={handleReset}
            className="border text-indigo-600 font-semibold py-2 px-4 rounded-full text-lg hover:scale-105 transition-all duration-300"
          >
            Try another image
          </button>
          <a
            href={resultImage}
            download="background-removed.png"
            className="cursor-pointer py-3 px-6 text-center text-white font-semibold rounded-full bg-gradient-to-r from-purple-500 to-indigo-500 shadow-lg hover:from-purple-600 hover:to-indigo-600 transition duration-300 ease-in-out transform scale-105"
          >
            Download image
          </a>
        </div>
      )}
    </div>
  );
};

export default Result;

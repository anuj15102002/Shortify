/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      backgroundImage: {
        "custom-gradient": "linear-gradient(to right, #0f0f0f, #1a1a1a)", // body bg
        "card-gradient": "linear-gradient(to right, #1a1a1a, #2a2a2a)",   // dark card
        "header-gradient": "linear-gradient(to right, #4f46e5, #9333ea)", // indigo to violet
        "footer-gradient": "linear-gradient(to right, #0d9488, #1e293b)", // teal to slate
      },
      colors: {
        background: "#0f0f0f",     // page bg
        surface: "#1a1a1a",        // cards
        text: "#f5f5f5",           // main text
        btnColor: "#333",          // button bg
        linkColor: "#60a5fa",      // link blue
        accent: "#6366f1",         // accent indigo
      },
      boxShadow: {
        custom: "0 0 15px rgba(255, 255, 255, 0.05)",
      },
      fontFamily: {
        roboto: ["Roboto", "sans-serif"],
        montserrat: ["Montserrat"],
      },
    },
  },
  plugins: [],
};

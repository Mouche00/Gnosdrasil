/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'neumorphic': {
          'bg': '#f0f0f0',
          'shadow-light': '#d1d1d1',
          'shadow-dark': '#ffffff',
        }
      },
      boxShadow: {
        'neumorphic': '8px 8px 16px #d1d1d1, -8px -8px 16px #ffffff',
        'neumorphic-inner': 'inset 4px 4px 8px #d1d1d1, inset -4px -4px 8px #ffffff',
      },
    },
  },
  plugins: [],
} 
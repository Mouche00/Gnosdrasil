/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'pastel': {
          'blue': '#7EB6FF',
          'pink': '#FF8FA3',
          'purple': '#B8A6FF',
          'green': '#7ED957',
          'yellow': '#FFD93D',
        },
        'soft': {
          'gray': '#F5F5F5',
          'dark': '#2D3748',
        }
      },
      boxShadow: {
        'neumorphic': '20px 20px 60px #d9d9d9, -20px -20px 60px #ffffff',
        'neumorphic-sm': '10px 10px 30px #d9d9d9, -10px -10px 30px #ffffff',
      },
      backgroundImage: {
        'gradient-radial': 'radial-gradient(var(--tw-gradient-stops))',
      }
    },
  },
  plugins: [],
} 
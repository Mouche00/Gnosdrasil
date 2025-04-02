import React from 'react';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  ChartOptions,
} from 'chart.js';
import { Line } from 'react-chartjs-2';
import { DailyJobCount } from '../types/jobs';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

interface JobCountChartProps {
  data: DailyJobCount[];
}

const JobCountChart: React.FC<JobCountChartProps> = ({ data }) => {
  const chartData = {
    labels: data.map(day => new Date(day.date).toLocaleDateString()),
    datasets: [
      {
        label: 'Daily Job Counts',
        data: data.map(day => day.count),
        borderColor: 'rgb(147, 197, 253)', // pastel-blue
        backgroundColor: 'rgba(147, 197, 253, 0.1)',
        tension: 0.4,
        fill: true,
      },
    ],
  };

  const options: ChartOptions<'line'> = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: false,
      },
      tooltip: {
        backgroundColor: 'rgba(0, 0, 0, 0.8)',
        padding: 12,
        titleColor: '#fff',
        bodyColor: '#fff',
        callbacks: {
          label: (context) => `Jobs: ${context.parsed.y}`,
        },
      },
    },
    scales: {
      y: {
        beginAtZero: true,
        grid: {
          color: 'rgba(0, 0, 0, 0.05)',
        },
        ticks: {
          stepSize: 1,
        },
      },
      x: {
        grid: {
          display: false,
        },
      },
    },
  };

  return (
    <div className="h-[200px] w-full">
      <Line data={chartData} options={options} />
    </div>
  );
};

export default JobCountChart; 
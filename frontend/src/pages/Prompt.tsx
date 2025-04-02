import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { apiService } from '../services/api';
import JobAnalysis from '../components/JobAnalysis';
import JobList from '../components/JobList';
import { JobResponse } from '../types/jobs';

interface PromptResponse {
  text: string;
  timestamp: string;
}

const Prompt: React.FC = () => {
  const { user } = useAuth();
  const [prompt, setPrompt] = useState('');
  const [responses, setResponses] = useState<PromptResponse[]>([]);
  const [jobData, setJobData] = useState<JobResponse | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const { data, error } = await apiService.post<JobResponse>('/prompt/jobs', {
        prompt: prompt.trim()
      });

      if (error) {
        throw new Error(error);
      }

      console.log(data);
      

      setJobData(data);
      const response: PromptResponse = {
        text: prompt,
        timestamp: new Date().toISOString(),
      };
      
      setResponses(prev => [response, ...prev]);
      setPrompt('');
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to generate response. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-[calc(100vh-6rem)] py-8">
      <div className="max-w-7xl mx-auto px-4">
        <div className="text-center mb-8">
          <h1 className="text-4xl font-bold bg-gradient-to-r from-pastel-blue via-pastel-purple to-pastel-pink bg-clip-text text-transparent">
            Job Market Analysis
          </h1>
          <p className="text-soft-dark mt-2">Get insights into the job market with our AI-powered analysis</p>
        </div>

        <div className="bg-soft-gray rounded-2xl shadow-neumorphic p-8 mb-8">
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label htmlFor="prompt" className="block text-sm font-medium text-soft-dark mb-2">
                Your Search Query
              </label>
              <textarea
                id="prompt"
                value={prompt}
                onChange={(e) => setPrompt(e.target.value)}
                className="w-full h-32 px-4 py-3 rounded-xl bg-white border border-gray-200 focus:border-pastel-blue focus:ring-2 focus:ring-pastel-blue/20 outline-none transition-all duration-300 resize-none"
                placeholder="Enter your job search criteria (e.g., 'Software Engineer in Morocco')..."
                required
              />
            </div>

            {error && (
              <div className="p-4 bg-red-50 border border-red-200 rounded-xl text-red-600">
                {error}
              </div>
            )}

            <button
              type="submit"
              disabled={loading || !prompt.trim()}
              className="w-full py-3 px-4 rounded-xl bg-gradient-to-r from-pastel-blue to-pastel-purple text-white font-medium shadow-neumorphic-sm hover:shadow-neumorphic transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {loading ? (
                <div className="flex items-center justify-center space-x-2">
                  <svg className="animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                  </svg>
                  <span>Analyzing...</span>
                </div>
              ) : (
                'Analyze Job Market'
              )}
            </button>
          </form>
        </div>

        {jobData && (
          <div className="space-y-8">
            <section className="bg-white rounded-2xl shadow-neumorphic p-6">
              <h2 className="text-2xl font-semibold text-soft-dark mb-6 flex items-center">
                <svg className="w-6 h-6 mr-2 text-pastel-blue" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                </svg>
                Market Insights
              </h2>
              <JobAnalysis data={jobData} />
            </section>

            <section className="bg-white rounded-2xl shadow-neumorphic p-6">
              <h2 className="text-2xl font-semibold text-soft-dark mb-6 flex items-center">
                <svg className="w-6 h-6 mr-2 text-pastel-purple" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                </svg>
                Job Listings
              </h2>
              <JobList jobs={jobData.jobs} />
            </section>
          </div>
        )}
      </div>
    </div>
  );
};

export default Prompt; 
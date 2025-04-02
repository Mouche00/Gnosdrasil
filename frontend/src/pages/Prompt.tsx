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
      const { data, error } = await apiService.post<JobResponse>('/prompt/process', {
        prompt: prompt.trim()
      });

      if (error) {
        throw new Error(error);
      }

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
            Create Your Content
          </h1>
          <p className="text-soft-dark mt-2">Let your imagination flow with our AI-powered content generator</p>
        </div>

        <div className="bg-soft-gray rounded-2xl shadow-neumorphic p-8 mb-8">
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label htmlFor="prompt" className="block text-sm font-medium text-soft-dark mb-2">
                Your Prompt
              </label>
              <textarea
                id="prompt"
                value={prompt}
                onChange={(e) => setPrompt(e.target.value)}
                className="w-full h-32 px-4 py-3 rounded-xl bg-white border border-gray-200 focus:border-pastel-blue focus:ring-2 focus:ring-pastel-blue/20 outline-none transition-all duration-300 resize-none"
                placeholder="Enter your creative prompt here..."
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
                  <span>Generating...</span>
                </div>
              ) : (
                'Generate Content'
              )}
            </button>
          </form>
        </div>

        {jobData && (
          <div className="space-y-8">
            <div>
              <h2 className="text-2xl font-semibold text-soft-dark mb-6">Job Analysis</h2>
              <JobAnalysis data={jobData.jobAnalysisDTO} />
            </div>

            <div>
              <h2 className="text-2xl font-semibold text-soft-dark mb-6">Job Listings</h2>
              <JobList jobs={jobData.jobAnalysisDTO.jobs} />
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Prompt; 
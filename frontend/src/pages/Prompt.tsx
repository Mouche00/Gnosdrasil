import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { apiService } from '../services/api';

interface PromptResponse {
  text: string;
  timestamp: string;
}

interface ApiPromptResponse {
  response: string;
}

const Prompt: React.FC = () => {
  const { user } = useAuth();
  const [prompt, setPrompt] = useState('');
  const [responses, setResponses] = useState<PromptResponse[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const { data, error } = await apiService.post<ApiPromptResponse>('/prompt/process', {
        prompt: prompt.trim()
      });

      if (error) {
        throw new Error(error);
      }

      const response: PromptResponse = {
        text: data.response,
        timestamp: new Date().toISOString(),
      };

      console.log(data);
      
      
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
      <div className="max-w-4xl mx-auto">
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

        <div className="space-y-6">
          <h2 className="text-2xl font-semibold text-soft-dark">Your Generated Content</h2>
          {responses.length === 0 ? (
            <div className="bg-soft-gray rounded-2xl shadow-neumorphic p-8 text-center">
              <div className="w-16 h-16 mx-auto mb-4 rounded-full bg-gradient-to-br from-pastel-blue to-pastel-purple flex items-center justify-center">
                <svg className="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
                </svg>
              </div>
              <p className="text-soft-dark">No content generated yet. Start by entering a prompt above!</p>
            </div>
          ) : (
            responses.map((response, index) => (
              <div key={index} className="bg-soft-gray rounded-2xl shadow-neumorphic p-6">
                <div className="flex items-start space-x-4">
                  <div className="w-10 h-10 rounded-full bg-gradient-to-br from-pastel-pink to-pastel-purple flex items-center justify-center text-white font-medium">
                    {user?.username[0].toUpperCase()}
                  </div>
                  <div className="flex-1">
                    <p className="text-soft-dark whitespace-pre-wrap">{response.text}</p>
                    <p className="text-sm text-gray-500 mt-2">
                      {new Date(response.timestamp).toLocaleString()}
                    </p>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default Prompt; 
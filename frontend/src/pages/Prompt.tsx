import React, { useState } from 'react';
import Button from '../components/Button';

const Prompt: React.FC = () => {
  const [prompt, setPrompt] = useState('');
  const [response, setResponse] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Handle prompt submission logic here
    console.log('Prompt submitted:', prompt);
    // Simulate API response
    setResponse('This is a simulated response to your prompt. Replace this with actual API integration.');
  };

  return (
    <div className="min-h-screen bg-gray-100 p-4">
      <div className="max-w-4xl mx-auto">
        <div className="bg-white rounded-2xl shadow-xl p-8">
          <h2 className="text-3xl font-bold text-gray-800 mb-6">AI Prompt Interface</h2>
          
          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="space-y-4">
              <label className="block text-gray-700 font-medium">
                Enter your prompt
              </label>
              <textarea
                value={prompt}
                onChange={(e) => setPrompt(e.target.value)}
                className="w-full h-32 px-4 py-3 rounded-xl bg-gray-100 shadow-inner
                         focus:outline-none focus:ring-2 focus:ring-gray-300
                         text-gray-700 placeholder-gray-400 resize-none"
                placeholder="Type your prompt here..."
              />
            </div>
            
            <Button type="submit" className="w-full">
              Submit Prompt
            </Button>
          </form>

          {response && (
            <div className="mt-8 space-y-4">
              <h3 className="text-xl font-semibold text-gray-800">Response:</h3>
              <div className="p-4 bg-gray-50 rounded-xl shadow-inner">
                <p className="text-gray-700 whitespace-pre-wrap">{response}</p>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Prompt; 
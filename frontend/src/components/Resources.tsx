import React, { useState } from 'react';
import { apiService } from '../services/api';
import { Resource } from '../types/jobs';
import ResourceViewer from './ResourceViewer';

interface ResourcesProps {
  stepId: string;
  stepLabel: string;
}

const Resources: React.FC<ResourcesProps> = ({ stepId, stepLabel }) => {
  const [resources, setResources] = useState<Resource[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [selectedResource, setSelectedResource] = useState<Resource | null>(null);

  const fetchResources = async () => {
    setLoading(true);
    setError(null);
    try {
      const { data, error } = await apiService.post<Resource[]>('/search/resources', {
        id: stepId,
        label: stepLabel
      });

      if (error) {
        throw new Error(error);
      }

      setResources(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch resources');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h3 className="text-lg font-medium text-soft-dark">Learning Resources</h3>
        <button
          onClick={fetchResources}
          disabled={loading}
          className="px-4 py-2 rounded-lg bg-pastel-blue text-white font-medium shadow-neumorphic-sm hover:shadow-neumorphic transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {loading ? (
            <div className="flex items-center space-x-2">
              <svg className="animate-spin h-4 w-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              <span>Loading...</span>
            </div>
          ) : (
            'Load Resources'
          )}
        </button>
      </div>

      {error && (
        <div className="p-3 bg-red-50 border border-red-200 rounded-lg text-red-600 text-sm">
          {error}
        </div>
      )}

      {resources.length > 0 && (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {resources.map((resource) => (
            <div
              key={resource.link}
              className="bg-soft-gray rounded-xl shadow-neumorphic p-4 hover:shadow-neumorphic-lg transition-all duration-200"
            >
              <h4 className="font-medium text-soft-dark mb-2">{resource.title}</h4>
              <p className="text-sm text-gray-600 mb-3 line-clamp-2">{resource.snippet}</p>
              <div className="flex items-center justify-between">
                <a
                  href={resource.link}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="text-pastel-blue text-sm hover:text-pastel-blue/80 transition-colors duration-200"
                >
                  Visit Source
                </a>
                <button
                  onClick={() => setSelectedResource(resource)}
                  className="px-3 py-1 rounded-lg bg-pastel-purple text-white text-sm hover:bg-pastel-purple/90 transition-colors duration-200"
                >
                  View Content
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {selectedResource && (
        <ResourceViewer
          html={selectedResource.pageSource}
          onClose={() => setSelectedResource(null)}
        />
      )}
    </div>
  );
};

export default Resources; 
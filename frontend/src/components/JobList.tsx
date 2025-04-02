import React, { useState } from 'react';
import { Job } from '../types/jobs';

interface JobListProps {
  jobs: Job[];
}

const JobList: React.FC<JobListProps> = ({ jobs }) => {
  const [expandedJob, setExpandedJob] = useState<string | null>(null);
  const ITEMS_TO_SHOW = 5;

  const toggleJob = (jobId: string) => {
    setExpandedJob(expandedJob === jobId ? null : jobId);
  };

  return (
    <div className="space-y-4">
      {jobs.slice(0, ITEMS_TO_SHOW).map((job) => (
        <div key={job.jobId} className="bg-soft-gray rounded-xl shadow-neumorphic p-4">
          <div className="flex justify-between items-start mb-3">
            <div className="flex-1 min-w-0">
              <h3 className="text-lg font-semibold text-soft-dark mb-1 truncate">{job.title}</h3>
              <div className="flex items-center space-x-2 text-sm">
                <span className="text-pastel-blue font-medium">{job.company}</span>
                <span className="text-gray-400">•</span>
                <span className="text-gray-600">{job.location}</span>
              </div>
            </div>
            <div className="flex flex-col items-end ml-4">
              <span className="text-sm text-pastel-purple font-medium">{job.jobType}</span>
              <span className="text-sm text-pastel-pink">{job.experienceLevel}</span>
            </div>
          </div>

          <div className="prose prose-sm max-w-none mb-3 line-clamp-3">
            <div dangerouslySetInnerHTML={{ __html: job.description }} />
          </div>

          <div className="flex justify-between items-center">
            <div className="flex items-center space-x-4 text-sm">
              <span className="text-gray-600">
                Posted: {new Date(job.datePosted).toLocaleDateString()}
              </span>
              {job.salary && (
                <>
                  <span className="text-gray-400">•</span>
                  <span className="text-gray-600">{job.salary}</span>
                </>
              )}
            </div>
            <div className="flex space-x-2">
              {job.applyUrl !== 'N/A' && (
                <a
                  href={job.applyUrl}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="px-3 py-1.5 bg-pastel-blue text-white text-sm rounded-lg hover:bg-pastel-blue/90 transition-colors duration-200"
                >
                  Apply
                </a>
              )}
              <a
                href={job.url}
                target="_blank"
                rel="noopener noreferrer"
                className="px-3 py-1.5 bg-soft-gray text-soft-dark text-sm rounded-lg hover:bg-gray-100 transition-colors duration-200"
              >
                Details
              </a>
            </div>
          </div>
        </div>
      ))}

      {jobs.length > ITEMS_TO_SHOW && (
        <div className="text-center">
          <button
            onClick={() => setExpandedJob(expandedJob === 'all' ? null : 'all')}
            className="text-pastel-purple font-medium hover:text-pastel-purple/80 transition-colors duration-200"
          >
            {expandedJob === 'all' ? 'Show Less' : `Show ${jobs.length - ITEMS_TO_SHOW} More Jobs`}
          </button>
        </div>
      )}
    </div>
  );
};

export default JobList; 
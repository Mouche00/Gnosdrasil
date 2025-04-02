import React from 'react';
import { Job } from '../types/jobs';

interface JobListProps {
  jobs: Job[];
}

const JobList: React.FC<JobListProps> = ({ jobs }) => {
  return (
    <div className="space-y-6">
      {jobs.map((job) => (
        <div key={job.jobId} className="bg-soft-gray rounded-2xl shadow-neumorphic p-6">
          <div className="flex justify-between items-start mb-4">
            <div>
              <h3 className="text-xl font-semibold text-soft-dark mb-1">{job.title}</h3>
              <p className="text-pastel-blue font-medium">{job.company}</p>
              <p className="text-gray-600 text-sm">{job.location}</p>
            </div>
            <div className="flex flex-col items-end">
              <span className="text-sm text-pastel-purple font-medium">{job.jobType}</span>
              <span className="text-sm text-pastel-pink">{job.experienceLevel}</span>
            </div>
          </div>

          <div className="prose prose-sm max-w-none mb-4" dangerouslySetInnerHTML={{ __html: job.description }} />

          <div className="flex justify-between items-center">
            <div className="flex items-center space-x-4">
              <span className="text-sm text-gray-600">
                Posted: {new Date(job.datePosted).toLocaleDateString()}
              </span>
              {job.salary && (
                <span className="text-sm text-gray-600">{job.salary}</span>
              )}
            </div>
            <div className="flex space-x-3">
              {job.applyUrl !== 'N/A' && (
                <a
                  href={job.applyUrl}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="px-4 py-2 bg-pastel-blue text-white rounded-xl hover:bg-pastel-blue/90 transition-colors duration-200"
                >
                  Apply Now
                </a>
              )}
              <a
                href={job.url}
                target="_blank"
                rel="noopener noreferrer"
                className="px-4 py-2 bg-soft-gray text-soft-dark rounded-xl hover:bg-gray-100 transition-colors duration-200"
              >
                View Details
              </a>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};

export default JobList; 
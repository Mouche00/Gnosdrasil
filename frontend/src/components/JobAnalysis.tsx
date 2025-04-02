import React from 'react';
import { JobAnalysisDTO } from '../types/jobs';

interface JobAnalysisProps {
  data: JobAnalysisDTO;
}

const JobAnalysis: React.FC<JobAnalysisProps> = ({ data }) => {
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      {/* Top Job Titles */}
      <div className="bg-soft-gray rounded-2xl shadow-neumorphic p-6">
        <h3 className="text-xl font-semibold text-soft-dark mb-4">Top Job Titles</h3>
        <div className="space-y-3">
          {data.topJobTitles.map((job, index) => (
            <div key={index} className="flex justify-between items-center">
              <span className="text-soft-dark">{job.title}</span>
              <span className="text-pastel-blue font-medium">{job.count}</span>
            </div>
          ))}
        </div>
      </div>

      {/* Top Skills */}
      <div className="bg-soft-gray rounded-2xl shadow-neumorphic p-6">
        <h3 className="text-xl font-semibold text-soft-dark mb-4">Top Skills</h3>
        <div className="space-y-3">
          {data.topSkills.map((skill, index) => (
            <div key={index} className="flex justify-between items-center">
              <span className="text-soft-dark capitalize">{skill.skill}</span>
              <span className="text-pastel-purple font-medium">{skill.count}</span>
            </div>
          ))}
        </div>
      </div>

      {/* Top Companies */}
      <div className="bg-soft-gray rounded-2xl shadow-neumorphic p-6">
        <h3 className="text-xl font-semibold text-soft-dark mb-4">Top Companies</h3>
        <div className="space-y-3">
          {data.topCompanies.map((company, index) => (
            <div key={index} className="flex justify-between items-center">
              <span className="text-soft-dark">{company.company}</span>
              <span className="text-pastel-pink font-medium">{company.count}</span>
            </div>
          ))}
        </div>
      </div>

      {/* Experience Level Distribution */}
      <div className="bg-soft-gray rounded-2xl shadow-neumorphic p-6">
        <h3 className="text-xl font-semibold text-soft-dark mb-4">Experience Levels</h3>
        <div className="space-y-3">
          {Object.entries(data.experienceLevelDistribution).map(([level, count]) => (
            <div key={level} className="flex justify-between items-center">
              <span className="text-soft-dark">{level}</span>
              <span className="text-pastel-blue font-medium">{count}</span>
            </div>
          ))}
        </div>
      </div>

      {/* Job Type Distribution */}
      <div className="bg-soft-gray rounded-2xl shadow-neumorphic p-6">
        <h3 className="text-xl font-semibold text-soft-dark mb-4">Job Types</h3>
        <div className="space-y-3">
          {Object.entries(data.jobTypeDistribution).map(([type, count]) => (
            <div key={type} className="flex justify-between items-center">
              <span className="text-soft-dark">{type}</span>
              <span className="text-pastel-purple font-medium">{count}</span>
            </div>
          ))}
        </div>
      </div>

      {/* Daily Job Counts */}
      <div className="bg-soft-gray rounded-2xl shadow-neumorphic p-6">
        <h3 className="text-xl font-semibold text-soft-dark mb-4">Daily Job Counts</h3>
        <div className="space-y-3">
          {data.dailyJobCounts.map((day, index) => (
            <div key={index} className="flex justify-between items-center">
              <span className="text-soft-dark">{new Date(day.date).toLocaleDateString()}</span>
              <span className="text-pastel-pink font-medium">{day.count}</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default JobAnalysis; 
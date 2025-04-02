import React, { useState } from 'react';
import { Roadmap as RoadmapType, RoadmapStep } from '../types/jobs';

interface RoadmapProps {
  data: RoadmapType;
}

const Roadmap: React.FC<RoadmapProps> = ({ data }) => {
  const [selectedStep, setSelectedStep] = useState<string | null>(null);

  const getStepById = (steps: RoadmapStep[], id: string): RoadmapStep | null => {
    for (const step of steps) {
      if (step.id === id) return step;
      const found = getStepById(step.connectedSteps, id);
      if (found) return found;
    }
    return null;
  };

  const selectedStepData = selectedStep ? getStepById(data.steps, selectedStep) : null;

  const renderTopicButton = (step: RoadmapStep) => (
    <button
      key={step.id}
      onClick={() => setSelectedStep(step.id === selectedStep ? null : step.id)}
      className={`
        px-3 py-1.5 rounded-lg text-sm font-medium transition-all duration-200
        ${step.id === selectedStep
          ? 'bg-pastel-blue text-white shadow-neumorphic-sm'
          : 'bg-soft-gray text-soft-dark hover:bg-pastel-blue/10 hover:text-pastel-blue'
        }
        shadow-neumorphic-sm hover:shadow-neumorphic
      `}
    >
      {step.label}
    </button>
  );

  const renderConnectedSteps = (steps: RoadmapStep[]) => (
    <div className="flex flex-wrap gap-2">
      {steps.map(step => renderTopicButton(step))}
    </div>
  );

  return (
    <div className="bg-white rounded-2xl shadow-neumorphic p-6">
      <h2 className="text-2xl font-semibold text-soft-dark mb-6 flex items-center">
        <svg className="w-6 h-6 mr-2 text-pastel-purple" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
        </svg>
        {data.title} Learning Roadmap
      </h2>

      <div className="space-y-6">
        {/* Main Topics */}
        <div className="space-y-3">
          <h3 className="text-lg font-medium text-soft-dark">Main Topics</h3>
          <div className="flex flex-wrap gap-2">
            {data.steps.map(step => renderTopicButton(step))}
          </div>
        </div>

        {/* Selected Topic Details */}
        {selectedStepData && selectedStepData.connectedSteps.length > 0 && (
          <div className="space-y-3">
            <h3 className="text-lg font-medium text-soft-dark">
              Related Topics for: {selectedStepData.label}
            </h3>
            {renderConnectedSteps(selectedStepData.connectedSteps)}
          </div>
        )}
      </div>
    </div>
  );
};

export default Roadmap; 
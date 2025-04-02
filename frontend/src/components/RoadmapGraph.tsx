import React, { useCallback, useMemo } from 'react';
import ReactFlow, {
  Node,
  Edge,
  Background,
  Controls,
  MiniMap,
  useNodesState,
  useEdgesState,
  Position,
} from 'reactflow';
import 'reactflow/dist/style.css';
import { Roadmap, Step } from '../types/roadmap';

interface RoadmapGraphProps {
  data: Roadmap;
}

const NODE_WIDTH = 200;
const NODE_HEIGHT = 40;
const LEVEL_SPACING = 100;
const SIBLING_SPACING = 20;

const RoadmapGraph: React.FC<RoadmapGraphProps> = ({ data }) => {
  const processSteps = useCallback((steps: Step[], level: number = 0, xOffset: number = 0): { nodes: Node[]; edges: Edge[] } => {
    const nodes: Node[] = [];
    const edges: Edge[] = [];
    let currentX = xOffset;

    steps.forEach((step, index) => {
      const nodeId = step.id;
      const y = level * (NODE_HEIGHT + LEVEL_SPACING);

      // Add the main node
      nodes.push({
        id: nodeId,
        position: { x: currentX, y },
        data: { label: step.label },
        type: 'custom',
        style: {
          background: '#fff',
          border: '1px solid #e2e8f0',
          borderRadius: '8px',
          padding: '8px',
          width: NODE_WIDTH,
          height: NODE_HEIGHT,
          boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
        },
      });

      // Process connected steps
      if (step.connectedSteps.length > 0) {
        const { nodes: childNodes, edges: childEdges } = processSteps(
          step.connectedSteps,
          level + 1,
          currentX - ((step.connectedSteps.length - 1) * (NODE_WIDTH + SIBLING_SPACING)) / 2
        );
        nodes.push(...childNodes);
        edges.push(...childEdges);

        // Add edges from current node to connected steps
        step.connectedSteps.forEach((connectedStep) => {
          edges.push({
            id: `${nodeId}-${connectedStep.id}`,
            source: nodeId,
            target: connectedStep.id,
            type: 'smoothstep',
            animated: true,
            style: { stroke: '#94a3b8' },
          });
        });
      }

      currentX += NODE_WIDTH + SIBLING_SPACING;
    });

    return { nodes, edges };
  }, []);

  const { nodes: initialNodes, edges: initialEdges } = useMemo(() => {
    return processSteps(data.steps);
  }, [data.steps, processSteps]);

  const [nodes, setNodes, onNodesChange] = useNodesState(initialNodes);
  const [edges, setEdges, onEdgesChange] = useEdgesState(initialEdges);

  return (
    <div className="w-full h-[600px] bg-white rounded-xl shadow-neumorphic">
      <ReactFlow
        nodes={nodes}
        edges={edges}
        onNodesChange={onNodesChange}
        onEdgesChange={onEdgesChange}
        fitView
        attributionPosition="bottom-left"
        nodeTypes={{
          custom: CustomNode,
        }}
      >
        <Background color="#f1f5f9" gap={16} />
        <Controls className="bg-white rounded-lg shadow-md" />
        <MiniMap className="bg-white rounded-lg shadow-md" />
      </ReactFlow>
    </div>
  );
};

const CustomNode: React.FC<{ data: { label: string } }> = ({ data }) => {
  return (
    <div className="px-4 py-2 text-sm font-medium text-soft-dark">
      {data.label}
    </div>
  );
};

export default RoadmapGraph; 
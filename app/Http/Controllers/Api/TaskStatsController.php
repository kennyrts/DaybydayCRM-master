<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Task;
use App\Models\Status;
use Illuminate\Http\JsonResponse;

class TaskStatsController extends Controller
{
    /**
     * Get the number of tasks by status
     *
     * @return JsonResponse
     */
    public function byStatus()
    {
        $stats = [];
        
        // Get all task statuses
        $statuses = Status::typeOfTask()->get();
        
        // Count tasks for each status
        foreach ($statuses as $status) {
            $stats[$status->title] = Task::where('status_id', $status->id)->count();
        }

        // Add total count
        $stats['total'] = Task::count();

        return response()->json([
            'success' => true,
            'data' => $stats
        ]);
    }
} 
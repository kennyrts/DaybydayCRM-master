@extends('layouts.master')

@section('heading')
{{ __('Export Database') }}
@endsection

@section('content')
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <div class="panel panel-default">
                <div class="panel-heading">{{ __('Export Database') }}</div>
                <div class="panel-body">
                    <div class="alert alert-info">
                        <strong>{{ __('Note:') }}</strong> {{ __('This will export all data from the database to a ZIP file containing CSV files for each table.') }}
                    </div>

                    <form method="POST" action="{{ route('database.export') }}">
                        {{ csrf_field() }}

                        <div class="form-group">
                            <button type="submit" class="btn btn-primary">
                                {{ __('Export Database') }}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
@endsection 
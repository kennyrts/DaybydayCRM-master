@extends('layouts.master')

@section('heading')
{{ __('Reset Database') }}
@endsection

@section('content')
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <div class="panel panel-default">
                <div class="panel-heading">{{ __('Reset Database') }}</div>
                <div class="panel-body">
                    <div class="alert alert-info">
                        <strong>{{ __('Note:') }}</strong> {{ __('This action will reset the database to its initial state while preserving all user accounts. All other data will be lost. This cannot be undone.') }}
                    </div>

                    <form method="POST" action="{{ route('database.delete') }}">
                        {{ csrf_field() }}

                        <div class="form-group">
                            <div class="checkbox">
                                <label>
                                    <input type="checkbox" name="confirm" value="1"> {{ __('I confirm that I want to reset the database while preserving user accounts') }}
                                </label>
                            </div>
                        </div>

                        <div class="form-group">
                            <button type="submit" class="btn btn-danger">
                                {{ __('Reset Database') }}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
@endsection 
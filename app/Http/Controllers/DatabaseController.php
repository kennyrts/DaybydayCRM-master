<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Repositories\FilesystemIntegration;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Schema;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Artisan;

class DatabaseController extends Controller
{
    /**
     * Affiche le formulaire de suppression de données
     */
    public function deleteForm()
    {
        // Récupérer la liste des tables disponibles pour la suppression
        $tables = $this->getAvailableTables();
        
        return view('database.delete', compact('tables'));
    }

    /**
     * Traite la suppression des données
     */
    public function delete(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'confirm' => 'required|boolean|accepted',
        ]);

        if ($validator->fails()) {
            return redirect()->back()
                ->withErrors($validator)
                ->withInput();
        }

        try {
            // Sauvegarder les utilisateurs
            $users = DB::table('users')->get();
            
            // Exécuter migrate:fresh --seed pour réinitialiser la base de données
            $exitCode = Artisan::call('migrate:fresh', [
                '--seed' => true,
                '--force' => true
            ]);

            if ($exitCode !== 0) {
                throw new \Exception("La commande migrate:fresh a échoué avec le code de sortie: $exitCode");
            }
            
            // Restaurer les utilisateurs (sauf l'utilisateur admin par défaut avec ID 1)
            foreach ($users as $user) {
                if ($user->id > 1) {  // Ne pas restaurer l'utilisateur admin par défaut
                    // Supprimer certains champs qui pourraient causer des problèmes
                    $userData = (array) $user;
                    unset($userData['id']);  // Laisser la base de données générer un nouvel ID
                    
                    // Insérer l'utilisateur
                    DB::table('users')->insert($userData);
                }
            }

            return redirect()->back()
                ->with('flash_message', 'La base de données a été réinitialisée avec succès tout en préservant les utilisateurs.');
        } catch (\Exception $e) {
            return redirect()->back()
                ->with('flash_message_warning', 'Erreur lors de la réinitialisation de la base de données : ' . $e->getMessage());
        }
    }

    /**
     * Affiche le formulaire d'importation CSV
     */
    public function importForm()
    {
        // Récupérer la liste des tables disponibles pour l'importation
        $tables = $this->getAvailableTables();
        
        return view('database.import', compact('tables'));
    }

    /**
     * Traite l'importation CSV
     */
    public function import(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'table' => 'required|string',
            'csv_file' => 'required|file|mimes:csv,txt',
        ]);

        if ($validator->fails()) {
            return redirect()->back()
                ->withErrors($validator)
                ->withInput();
        }

        $table = $request->input('table');
        
        // Vérifier que la table est autorisée
        if (!in_array($table, $this->getAvailableTables())) {
            return redirect()->back()
                ->with('flash_message_warning', 'Table non autorisée pour l\'importation');
        }

        try {
            $file = $request->file('csv_file');
            $path = $file->getRealPath();
            
            // Lire le fichier CSV
            $data = array_map('str_getcsv', file($path));
            
            // Récupérer les en-têtes (première ligne)
            $headers = array_shift($data);
            
            // Vérifier que les colonnes existent dans la table
            $tableColumns = Schema::getColumnListing($table);
            foreach ($headers as $header) {
                if (!in_array($header, $tableColumns)) {
                    return redirect()->back()
                        ->with('flash_message_warning', 'La colonne "' . $header . '" n\'existe pas dans la table');
                }
            }
            
            // Préparer les données pour l'insertion
            $records = [];
            foreach ($data as $row) {
                $record = [];
                foreach ($row as $key => $value) {
                    $record[$headers[$key]] = $value;
                }
                $records[] = $record;
            }
            
            // Insérer les données
            DB::table($table)->insert($records);
            
            return redirect()->back()
                ->with('flash_message', 'Les données ont été importées avec succès');
        } catch (\Exception $e) {
            return redirect()->back()
                ->with('flash_message_warning', 'Erreur lors de l\'importation : ' . $e->getMessage());
        }
    }

    /**
     * Affiche le formulaire d'exportation CSV
     */
    public function exportForm()
    {
        return view('database.export');
    }

    /**
     * Traite l'exportation CSV de toutes les tables
     */
    public function export(Request $request)
    {
        try {
            // Récupérer toutes les tables autorisées
            $tables = $this->getAvailableTables();
            
            // Créer un dossier temporaire pour stocker les fichiers CSV
            $tempDir = storage_path('app/temp_export_' . time());
            if (!file_exists($tempDir)) {
                mkdir($tempDir, 0755, true);
            }
            
            // Créer un fichier CSV pour chaque table
            foreach ($tables as $table) {
                $data = DB::table($table)->get();
                
                if (!$data->isEmpty()) {
                    $filePath = $tempDir . '/' . $table . '.csv';
                    $file = fopen($filePath, 'w');
                    
                    // Ajouter les en-têtes (noms des colonnes)
                    fputcsv($file, array_keys((array)$data[0]));
                    
                    // Ajouter les données
                    foreach ($data as $row) {
                        fputcsv($file, (array)$row);
                    }
                    
                    fclose($file);
                }
            }
            
            // Créer un fichier ZIP contenant tous les fichiers CSV
            $zipFileName = 'database_export_' . date('Y-m-d_His') . '.zip';
            $zipFilePath = storage_path('app/' . $zipFileName);
            
            $zip = new \ZipArchive();
            if ($zip->open($zipFilePath, \ZipArchive::CREATE) === TRUE) {
                $files = new \RecursiveIteratorIterator(
                    new \RecursiveDirectoryIterator($tempDir),
                    \RecursiveIteratorIterator::LEAVES_ONLY
                );
                
                foreach ($files as $file) {
                    if (!$file->isDir()) {
                        $filePath = $file->getRealPath();
                        $relativePath = basename($filePath);
                        
                        $zip->addFile($filePath, $relativePath);
                    }
                }
                
                $zip->close();
                
                // Supprimer le dossier temporaire
                $this->deleteDirectory($tempDir);
                
                // Télécharger le fichier ZIP
                return response()->download($zipFilePath)->deleteFileAfterSend(true);
            } else {
                throw new \Exception("Impossible de créer le fichier ZIP");
            }
        } catch (\Exception $e) {
            return redirect()->back()
                ->with('flash_message_warning', 'Erreur lors de l\'exportation : ' . $e->getMessage());
        }
    }

    /**
     * Supprime récursivement un répertoire et son contenu
     */
    private function deleteDirectory($dir) {
        if (!file_exists($dir)) {
            return true;
        }
        
        if (!is_dir($dir)) {
            return unlink($dir);
        }
        
        foreach (scandir($dir) as $item) {
            if ($item == '.' || $item == '..') {
                continue;
            }
            
            if (!$this->deleteDirectory($dir . DIRECTORY_SEPARATOR . $item)) {
                return false;
            }
        }
        
        return rmdir($dir);
    }

    /**
     * Récupère la liste des tables disponibles pour la manipulation
     */
    private function getAvailableTables()
    {
        // Liste complète des tables de la base de données
        // Vous pouvez exclure certaines tables si nécessaire
        $excludedTables = [];
        
        $tables = [];
        $dbTables = DB::select('SHOW TABLES');
        $dbName = 'Tables_in_' . env('DB_DATABASE'); // Ajustez selon votre configuration
        
        foreach ($dbTables as $table) {
            $tableName = $table->$dbName;
            if (!in_array($tableName, $excludedTables)) {
                $tables[] = $tableName;
            }
        }
        
        return $tables;
    }
} 
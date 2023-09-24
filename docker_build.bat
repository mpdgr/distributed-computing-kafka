cd job-controller
docker build -t job-controller .
cd ../standard-worker
docker build -t standard-worker .
cd ../super-worker
docker build -t super-worker .
cd ../task-scheduler
docker build -t task-scheduler .


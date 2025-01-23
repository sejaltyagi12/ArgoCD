var gulp = require('gulp');
var gutil = require('gulp-util');
var bower = require('bower');
var concat = require('gulp-concat');
var minifyCss = require('gulp-minify-css');
var rename = require('gulp-rename');
var inject = require('gulp-inject');
var shell = require('gulp-shell');
const del = require('del');
var rename = require('gulp-rename');
var uglify = require('gulp-uglify');

var paths = {
  css: 'src/content/css/*.css',
  controllers: 'src/app/modules/**/*.js',
  services: 'src/app/services/*.js',
  factories: 'src/app/common/factories/*.js',
  directives :  'src/app/common/directive/*.js',
  interceptor:  'src/app/common/api/*.js',
  config:  'src/app/common/core/*.js',
  otherJs: 'src/app/common/*.js',
  customJs:'src/content/js/*.js'
};

gulp.task('js', function () {
  return gulp.src(['src/app/**/*.js', 'src/content/**/*.js'])
     .pipe(uglify())
     .pipe(concat('app.js'))
     .pipe(gulp.dest('build'));
});

gulp.task('css',function(){
  return gulp.src('src/content/css/**/*') 
  .pipe(gulp.dest('build/css'));
});

gulp.task('fonts',function(){
  return gulp.src('src/content/fonts/**/*') 
  .pipe(gulp.dest('build/fonts'));
});

gulp.task('images',function(){
  return gulp.src('src/content/img/**/*') 
  .pipe(gulp.dest('build/content/img'));
});

gulp.task('html',function(){
  return gulp.src('src/app/**/*.html') 
  .pipe(gulp.dest('build/app'));
});

gulp.task('index',function(){
  return gulp.src('main.html') 
  .pipe(rename('index.html'))
  .pipe(gulp.dest('build'));
});

gulp.task('vendorjs',function(){
  return gulp.src([
    'src/bower_components/jquery/dist/jquery.min.js',
    'src/bower_components/jquery-ui/jquery-ui.min.js',
    'src/bower_components/bootstrap/dist/js/bootstrap.min.js',
    'src/bower_components/angular/angular.min.js',
    'src/bower_components/angular-bootstrap/ui-bootstrap-tpls.min.js',
    'src/bower_components/angular-ui-router/release/angular-ui-router.min.js',
    'src/bower_components/nprogress/nprogress.js',
    'src/bower_components/angular-cookies/angular-cookies.min.js',
    'src/bower_components/angular-animate/angular-animate.min.js',
    'src/bower_components/datatables.net/js/jquery.dataTables.min.js',
    'src/bower_components/angular-datatables/dist/angular-datatables.min.js',
    'src/bower_components/ng-file-upload-shim/ng-file-upload-shim.min.js',
    'src/bower_components/ng-file-upload/ng-file-upload.min.js',
    'src/bower_components/highcharts/highcharts.js',
    'src/bower_components/ng-img-crop/compile/minified/ng-img-crop.js',
    'src/bower_components/angularjs-dropdown-multiselect/dist/angularjs-dropdown-multiselect.min.js',
    'src/bower_components/angular-file-saver/dist/angular-file-saver.bundle.js',
    'src/bower_components/AngularJS-Toaster/toaster.min.js',
    'src/bower_components/moment/moment.js'
  ])
  .pipe(concat('vendor.js'))
  .pipe(gulp.dest('build/vendor'));
});

gulp.task('vendorcss',function(){
  return gulp.src([
    'src/bower_components/bootstrap/dist/css/bootstrap.min.css',
    'src/bower_components/font-awesome/css/font-awesome.min.css',
    'src/bower_components/nprogress/nprogress.css',
    'src/bower_components/jquery-ui/themes/smoothness/jquery-ui.min.css',
    'src/bower_components/datatables.net-dt/css/jquery.dataTables.min.css',
    'src/bower_components/AngularJS-Toaster/toaster.min.css'
  ]) 
  .pipe(gulp.dest('build/vendor'));
});

gulp.task('clean', function(){
  return del('build/**/*', {force:true});
});

gulp.task('build',  gulp.series('clean',  gulp.parallel('index','js', 'css', 'fonts', 'images','html', 'vendorjs', 'vendorcss')));

// Tasks
// gulp.task('default', ['sass']);
gulp.task('default', injectFiles);
// gulp.task('install', startInstall);
// gulp.task('sass', compileSass);
// gulp.task('install', ['git-check'], startInstall);
// gulp.task('git-check', gitCheck);
gulp.task('injectFiles', injectFiles);

// gulp.task('startApp', ['injectFiles'], shell.task([
//   'ionic serve'
// ]));

// gulp.task('watch', ['sass'], function() {
//   gulp.watch(paths.sass, ['sass']);
// });

function compileSass(done) {
  gulp.src('./scss/ionic.app.scss')
    .pipe(sass())
    .on('error', sass.logError)
    .pipe(gulp.dest('./www/css/'))
    .pipe(minifyCss({
      keepSpecialComments: 0
    }))
    .pipe(rename({ extname: '.min.css' }))
    .pipe(gulp.dest('./www/css/'))
    .on('end', done);
}

function startInstall() {
  return bower.commands.install()
    .on('log', function(data) {
      gutil.log('bower', gutil.colors.cyan(data.id), data.message);
    });
}

// function gitCheck(done) {
//   if (!sh.which('git')) {
//     console.log(
//       '  ' + gutil.colors.red('Git is not installed.'),
//       '\n  Git, the version control system, is required to download Ionic.',
//       '\n  Download git here:', gutil.colors.cyan('http://git-scm.com/downloads') + '.',
//       '\n  Once git is installed, run \'' + gutil.colors.cyan('gulp install') + '\' again.'
//     );
//     process.exit(1);
//   }
//   done();
// }

// Task to inject the JS and CSS files in
function injectFiles(){
  console.log("injecting JS and CSS Files to index.html");
  gulp.src('src/index.html')
    .pipe(inject(
      gulp.src([paths.otherJs,paths.interceptor,paths.config,paths.controllers, paths.directives, paths.services, paths.factories,paths.customJs, paths.css], {read: false}), {
        //    Adding current time stamp to the files so as to implement
        
        transform: function (filepath) {
          
          if (filepath.indexOf('.js') !== -1) {
            
            return '<script src="'+ filepath.replace('src/','')+ '?v=' + Date.now().toString() + '"></script>'
          }
          if (filepath.indexOf('.css') !== -1) {
            return '<link href="'+filepath.replace('src/','') + '?v=' + Date.now().toString() + '" rel="stylesheet">'
          }
          // Use the default transform as fallback:
          return inject.transform.apply(inject.transform, arguments);
        }
      }
    ))
  .pipe(gulp.dest('src/'));
 

}
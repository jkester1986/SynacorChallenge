let Combinatorics = require('js-combinatorics');

let coins = [2,7,3,9,5];

let perms = Combinatorics.permutation(coins).toArray();
console.log(3+ 5*Math.pow(9,2) + Math.pow(2,3) - 7);

perms.forEach(function(perm) {
    if((perm[0] + perm[1] * Math.pow(perm[2],2) + Math.pow(perm[3],3) - perm[4]) === 399) {
        console.log(perm);
        return;
    }
});

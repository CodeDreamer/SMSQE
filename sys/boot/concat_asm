; Concatenate list of files for QDOS job
;
; This requires the main routine of the job to have
;
;       strict section ordering
;               base                    the QDOS "special job" header
;               special                 empty for the special job code here
;               main                    the main part of the program
;                                       (the boot_concat routine is in main)
;
;       the data declaration
;
        section main

        xdef    boot_concat

        include 'dev8_keys_err'
        include 'dev8_keys_qdos_io'
        include 'dev8_keys_qdos_ioa'
        include 'dev8_keys_qdos_sms'
        include 'dev8_mac_cksum'

dt_buff    equ  $00
dt_flen    equ  $00                      ; file length in buffer
dt_ftyp    equ  $05                      ; file type
dt_datasp  equ  $06                      ; data space
dt.buflen  equ  $40                      ; data buffer length
dt_head    equ  $40
dt.headlen equ  $0e

dt_cksum   equ  $50                      ; long word checksum

dt_top     equ  $60

;+++
; This routine loads and concatenates a number of files into memory.
; All files must be an even number of bytes long.
; Each file is preceded by its length (long word) and its checksum (long word)
; The last file is followed by a zero long word.
; If used to concatenate executables, the dataspace replaces the checksum
;
;       d2  r   length of total concatenated file (total files + 4|8*nfiles+4)
;       d5 c  p number of files to concatenate
;       d6 c  p lsw spare space required at start, msbit set if dataspace req
;       a1  r   pointer to concatenated file (length of first file)
;       a2  r   pointer to first file header
;       a5 c  u pointer to table of file channel IDs, updated to end
;       a6 c  p pointer to data area (at least $60 bytes)
;
;       status return standard
;
;---
boot_concat
bc.reg  reg     d1/d3/d5/d7/a0/a4
        movem.l bc.reg,-(sp)
stk_nfil equ    $08

; first read all the file headers to find the size of memory to allocate

        lea     (a5),a2                  ; file list
        moveq   #0,d7                    ; accumulate length
        move.w  d6,d7                    ; plus spare
        move.l  (a2),a0                  ; get header of first file
        lea     dt_head(a6),a1
        moveq   #iof.rhdr,d0             ; read header
        moveq   #dt.headlen,d2
        bsr.s   bc_io

bc_fhloop
        move.l  (a2)+,a0                 ; next file
        lea     dt_buff(a6),a1
        moveq   #iof.rhdr,d0             ; read header
        moveq   #dt.headlen,d2
        bsr.s   bc_io

        add.l   dt_flen(a6),d7           ; file length
        addq.l  #8,d7                    ; + length + checksum
bc_fhnext
        subq.w  #1,d5
        bgt.s   bc_fhloop                ; ... and the next

; now we know how long the concatenated files are going to be we can allocate

        moveq   #4,d1                    ; spare at start
        add.l   d7,d1                    ; plus length of code
        moveq   #myself,d2
        moveq   #sms.achp,d0
        trap    #do.sms2                 ; allocate it
        tst.l   d0
        bne.s   bc_exit

; now load all the files

        lea     (a0,d6.w),a4             ; save base of files

        lea     (a4),a1                  ; and load files
        move.l  stk_nfil(sp),d5

bc_flloop
        move.l  (a5)+,a0                 ; get next file
        move.l  a1,-(sp)
        moveq   #iof.rhdr,d0             ; read header
        moveq   #dt.headlen,d2
        bsr.s   bc_io
        move.l  (sp)+,a1
        move.l  (a1)+,d2                 ; length of next file
        move.l  2(a1),(a1)+              ; dataspace (or checksum)
        move.l  a1,a2

        moveq   #iof.load,d0             ; and load
        bsr.s   bc_io
        tst.l   d6                       ; dataspace?
        bmi.s   bc_fllend
        move.l  a2,a1                    ; make checksum
        lwcksum a1,d2,d3,d0,d1
        move.l  d3,-(a2)                 ; and save it

bc_fllend
        subq.w  #1,d5
        bgt.s   bc_flloop                ; ... and the next

        clr.l   (a1)+                    ; zero at end
        move.l  a1,d2
        move.l  a4,a1                    ; base of file
        sub.l   a1,d2                    ; length of file

        lea     dt_head(a6),a2           ; header of first file

        moveq   #0,d0

bc_exit
        movem.l (sp)+,bc.reg
bc_rts
        rts

bc_io
        moveq   #-1,d3                   ; wait forever
        trap    #do.io                   ; do IO
        tst.l   d0
        beq.s   bc_rts
        addq.l  #4,sp                    ; on error, give up
        bra.s   bc_exit

bc_ipar
        moveq   #err.ipar,d0
        bra.s   bc_exit



        section special

; this code is called as a routine by ex to open the channels

ex_entry
        move.l  a0,a2           ; put the utility address somewhere safer
        exg     a5,a3           ; we will process the params from the back end
        moveq   #err.ipar,d0    ; preset error to invalid parameter
        subq.l  #2,d7           ; there must be at least 2 channels
        blt.s   ex_rts          ; ... but there aren't
        moveq   #0,d0           ; ... oh yes there are!
ex_loop
        subq.l  #8,a3           ; move down one
        cmp.l   a5,a3           ; any more parameters
        blt.s   ex_rts          ; ... no
        jsr     (a2)            ; get a string from the command line
        blt.s   ex_rts          ; ... oops
        bgt.s   ex_put_id       ; ... #n
        moveq   #ioa.kshr,d3    ; the file is an input
        tst.w   d5              ; last file?
        bne.s   ex_open         ; ... no
        moveq   #ioa.kovr,d3    ; ... yes, open overwrite
ex_open
        jsr     2(a2)           ; open the file
        bne.s   ex_rts          ; ... oops
ex_put_id
        move.l  a0,-(a4)        ; put the id on the stack
        addq.l  #1,d5           ; one more
        bra.s   ex_loop         ; and look at next
ex_rts
        rts

        end
